package service;

import kafka.BasicProducer;
import kafka.PassOutdatedConsumer;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Startup
@Singleton
public class StartUp {

    @Inject
    PassOutdatedConsumer consumer;

    @PostConstruct
    private void startup() {
        System.out.println(" ******* Starting generator! ******* ");
        consumer.start();
    }
}
