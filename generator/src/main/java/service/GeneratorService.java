package service;

import domain.PasswordGenerator;
import enums.ReadyStatus;
import utils.HealthCheck;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

@Stateless
public class GeneratorService extends HealthCheck {
    private final int passLength = 20;
    private PasswordGenerator generator;

    @PostConstruct
    private void init() {
        generator = new PasswordGenerator.PasswordGeneratorBuilder()
                .useDigits(true)
                .useLower(true)
                .useUpper(true)
                .usePunctuation(true)
                .build();

        setStatus(ReadyStatus.AVAILABLE);
    }

    public String generatePassword(int length) {
        return generator.generate(length);
    }

    public String generatePassword() {
        return generator.generate(passLength);
    }
}
