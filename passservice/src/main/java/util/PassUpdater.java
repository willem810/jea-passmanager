package util;

import domain.Password;
import kafka.PassOutdatedProducer;
import service.PasswordService;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Stateless
@Singleton
@Startup
public class PassUpdater {

    @Inject
    PasswordService passwordService;

    @Inject
    PassOutdatedProducer producer;

    ScheduledExecutorService executor;

    private static final int updateInterval = 10; // in minutes

    @PostConstruct
    private void init() {
        executor = Executors.newScheduledThreadPool(1);
        Start();
    }

    public void Start() {
        executor.scheduleAtFixedRate(createUpdateTask(), 0, updateInterval, TimeUnit.MINUTES);
    }

    public void Stop() {
        executor.shutdown();
    }

    private Runnable createUpdateTask() {
        return new Runnable() {
            public void run() {
                updatePasswords();

            }
        };
    }

    public void updatePasswords() {
        System.out.println("Updating passwords...");
        for (Password pass : passwordService.getAllPasswords()) {
            if (pass.expired()) {
                try {
                    System.out.println("Found outdated password!");
                    producer.sendMessage(pass);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }
}
