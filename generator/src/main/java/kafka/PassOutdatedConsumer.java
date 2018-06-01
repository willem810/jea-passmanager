package kafka;

import com.google.common.primitives.Longs;
import domain.Password;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeader;
import rest.client.AuthClient;
import rest.client.PasswordClient;
import service.GeneratorService;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Stateless
@Singleton
public class PassOutdatedConsumer extends BasicConsumer {

    public static String TOPIC = System.getenv("PASS_OUTDATED_TOPIC");
    public static String PARTITION_STRING = System.getenv("PASS_OUTDATED_PARTITION");
    public int partition;


    private static ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);

    @Inject
    PasswordClient passwordClient;

    @Inject
    GeneratorService generatorService;


    @PostConstruct
    private void init() {
        if (TOPIC == null) {
            TOPIC = "PassOutdatedTopic";
            System.out.println("Set 'TOPIC' to the defaults: " + TOPIC + ". Please consider injecting your own SysVar for TOPIC");
            //TOPIC = "test1";
        }
        if (PARTITION_STRING == null) {
            partition = 0;
            System.out.println("Set 'PARTITION_STRING' to the defaults: " + partition + ". Please consider injecting your own SysVar for PARTITION_STRING");
        } else {
            partition = Integer.valueOf(PARTITION_STRING);
        }

        this.Subscribe(TOPIC);
    }


    @Override
    protected void onMessage(ConsumerRecord<Long, String> record) {
        Headers headers = record.headers();

        byte[] passwordIdBytes = getHeaderValue("passid", headers);
        byte[] passUserBytes = getHeaderValue("passuser", headers);
        byte[] passServiceBytes = getHeaderValue("passservice", headers);

        long passwordId = Longs.fromByteArray(passwordIdBytes);
        String passUser = new String(passUserBytes, StandardCharsets.UTF_8);
        String passService = new String(passServiceBytes, StandardCharsets.UTF_8);


        System.out.println("Got an outdated password (id: " + passwordId + ") for " + passUser + " - " + passService);
        Password newPass = new Password(passwordId, generatorService.generatePassword(), passService, passUser);

        passwordClient.updatePassword(newPass);
    }

    private byte[] getHeaderValue(String headerKey, Headers headers) {
        Header hdr = headers.lastHeader(headerKey);
        if (hdr == null) {
            return new byte[0];
        }


        return hdr.value();
    }
}
