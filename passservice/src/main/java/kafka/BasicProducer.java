package kafka;

import enums.ReadyStatus;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import util.HealthCheck;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Stateless
@Singleton
abstract public class BasicProducer extends HealthCheck {
    protected static String BOOTSTRAP_SERVERS = System.getenv("BOOTSTRAP_SERVERS");

    private Producer<Long, String> producer;

    @PostConstruct
    private void init() {
        setStatus(ReadyStatus.STARTING);
        System.out.println("**** INITIALIZING BASIC PRODUCING *******");
        if (BOOTSTRAP_SERVERS == null) {
            BOOTSTRAP_SERVERS = "192.168.0.20:9092,10.0.75.1:9092,http://192.168.0.20:9092,http://10.0.75.1:9092";
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


        System.out.println("**** PROPERTY SETTINGS: ");
        System.out.println(props.stringPropertyNames());
        System.out.println(props.propertyNames());


        return new KafkaProducer<>(props);
    }

    public RecordMetadata sendMessage(ProducerRecord<Long, String> record) throws Exception {

        System.out.println("sending record: " + record.toString());
        Future<RecordMetadata> r = producer.send(record);
        System.out.println("created future...");


        return r.get(120, TimeUnit.SECONDS);
    }
}
