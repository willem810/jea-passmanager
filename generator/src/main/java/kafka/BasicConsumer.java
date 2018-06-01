package kafka;

import enums.ReadyStatus;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import utils.HealthCheck;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import java.util.Collections;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
@Singleton
abstract public class BasicConsumer extends HealthCheck {
    protected abstract void onMessage(ConsumerRecord<Long, String> record);

    protected static String BOOTSTRAP_SERVERS = System.getenv("BOOTSTRAP_SERVERS");

    Consumer<Long, String> consumer;

    Thread consumingThread;

    @PostConstruct
    public void create() {
        setStatus(ReadyStatus.STARTING);
        if (BOOTSTRAP_SERVERS == null) {
            BOOTSTRAP_SERVERS = "192.168.0.11:9092,localhost:9093,localhost:9094";
        }
        if (consumer == null) {
            consumer = createConsumer();
        }

        if (consumingThread == null) {
            consumingThread = createConsumingThread();
        }
        setStatus(ReadyStatus.UNAVAILABLE);
    }

    public void start() {
        if (consumer.subscription().size() <= 0) {
            Logger.getLogger(BasicConsumer.class.toString()).log(Level.WARNING, "Consumer not yet subscribed to a topic!");
        }

        if (consumingThread.isAlive()) {
            Logger.getLogger(BasicConsumer.class.toString()).log(Level.WARNING, "Consumer already running!");

            return;
        }

        Logger.getLogger(BasicConsumer.class.toString()).log(Level.WARNING, "Starting consumer...");
        consumingThread.start();
        setStatus(ReadyStatus.AVAILABLE);
        Logger.getLogger(BasicConsumer.class.toString()).log(Level.WARNING, "Consumer started!");

    }

    public void restart() {
        setStatus(ReadyStatus.STARTING);
        if (consumingThread.isAlive()) {
            consumingThread.interrupt();
        }
        consumingThread.start();
    }

    @PreDestroy
    public void close() {
        if (consumer != null) {
            consumer.close();
        }

        if (consumingThread != null) {
            consumingThread.interrupt();
        }
        setStatus(ReadyStatus.UNAVAILABLE);
    }

    public void Subscribe(String topic) {
        if (consumer == null) {
            consumer = createConsumer();
        }

        if (!isSubscribed(topic)) {
            // Subscribe to the topic.
            consumer.subscribe(Collections.singletonList(topic));
        }
    }

    public boolean isSubscribed(String topic) {
        if (consumer == null) {
            consumer = createConsumer();
        }

        for (String sub : consumer.subscription()) {
            if (sub.equals(topic)) {
                return true;
            }
        }

        return false;
    }

    private Consumer<Long, String> createConsumer() {
        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG,
                "KafkaExampleConsumer");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                LongDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());
        // Create the consumer using props.
        final Consumer<Long, String> consumer =
                new KafkaConsumer<>(props);
        return consumer;
    }

    private Thread createConsumingThread() {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                while (!consumingThread.isInterrupted()) {
                    final ConsumerRecords<Long, String> consumerRecords =
                            consumer.poll(1000);
                    if (consumerRecords.count() == 0) {
                        continue;
                    }

                    consumerRecords.forEach(record -> {
                        onMessage(record);
                    });

                    consumer.commitAsync();
                }

            }
        });
    }

}
