package rest;

import interceptor.LogError;
import interceptor.Logging;
import kafka.BasicConsumer;
import kafka.BasicProducer;
import kafka.PassOutdatedConsumer;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("kafka")
@Logging
@LogError
public class KafkaResource {
    @Inject
    BasicProducer producer;

    @Inject
    PassOutdatedConsumer consumer;



    @POST
    @Path("producer/stop")
    public void stopProducer() {
        try {
            producer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @POST
    @Path("consumer/start")
    public void runConsumer() {
        try {
            consumer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @POST
    @Path("consumer/stop")
    public void stopConsumer() {
        try {
            consumer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
