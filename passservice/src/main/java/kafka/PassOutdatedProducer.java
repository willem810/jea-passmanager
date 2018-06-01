package kafka;

import com.google.common.primitives.Longs;
import domain.Password;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

@Stateless
@Singleton
public class PassOutdatedProducer extends BasicProducer {

    public static String TOPIC = System.getenv("PASS_OUTDATED_TOPIC");
    public static String PARTITION_STRING = System.getenv("PASS_OUTDATED_PARTITION");
    public int partition;


    @PostConstruct
    private void init() {
        if (TOPIC == null) {
            TOPIC = "PassOutdatedTopic";
            System.out.println("Set 'TOPIC' to the defaults: " + TOPIC + ". Please consider injecting your own SysVar for TOPIC");

            TOPIC = "test1";
        }
        if (PARTITION_STRING == null) {
            partition = 0;
            System.out.println("Set 'PARTITION_STRING' to the defaults: " + partition + ". Please consider injecting your own SysVar for PARTITION_STRING");
        } else {
            partition = Integer.valueOf(PARTITION_STRING);
        }

        System.out.println(" *******  Starting Producing on boostrap server: " + this.BOOTSTRAP_SERVERS + ", producing on topic: " + TOPIC);
    }

    public RecordMetadata sendMessage(Password pass) throws Exception {

        List<Header> headers = new ArrayList<>();
        headers.add(new RecordHeader("passid", Longs.toByteArray(pass.getId())));
        headers.add(new RecordHeader("passuser", pass.getUsername().getBytes()));
        headers.add(new RecordHeader("passservice", pass.getService().getBytes()));
        headers.add(new RecordHeader("userjwt", pass.getService().getBytes()));

        ProducerRecord<Long, String> record
                = new ProducerRecord<Long, String>(TOPIC, partition, System.currentTimeMillis(), pass.getId(), "Password expired!", headers);

        return super.sendMessage(record);
    }

    @Override
    public String getServiceName() {
        return getClass().getName();
    }
}
