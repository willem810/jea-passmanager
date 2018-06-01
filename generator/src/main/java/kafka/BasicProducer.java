package kafka;

import enums.ReadyStatus;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import utils.HealthCheck;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import java.util.Properties;

@Stateless
@Singleton
abstract public class BasicProducer extends HealthCheck {
    protected static String BOOTSTRAP_SERVERS = System.getenv("BOOTSTRAP_SERVERS");

    private Producer<Long, String> producer;

    @PostConstruct
    public void init() {
        setStatus(ReadyStatus.STARTING);
        if (BOOTSTRAP_SERVERS == null) {
            BOOTSTRAP_SERVERS = "192.168.0.11:9092,localhost:9093,localhost:9094";
        }

        if (producer == null) {
            producer = createProducer();
        }
        setStatus(ReadyStatus.AVAILABLE);
    }

    @PreDestroy
    public void close() {
        if (producer != null) {
            producer.flush();
            producer.close();
        }

    }


    private Producer<Long, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaExampleProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName());
        return new KafkaProducer<>(props);
    }

    public RecordMetadata sendMessage(ProducerRecord<Long, String> record) throws Exception {
        return producer.send(record).get();
    }
}
