package utils;

import enums.ReadyStatus;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class HealthCheck {
    public static List<HealthCheck> checks = new ArrayList<>();
    private ReadyStatus status = ReadyStatus.NOT_STARTED;

    @PostConstruct
    public void initHealthCheck() {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Adding a new healthcheck class" + this.getClass().getName());
        checks.add(this);
    }

    public String getServiceName() {
        return this.getClass().getName();
    }


    public void setStatus(ReadyStatus newStatus) {
        this.status = newStatus;
    }

    public ReadyStatus getStatus() {
        return status;
    }

}
