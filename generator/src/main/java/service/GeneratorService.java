package service;

import domain.PasswordGenerator;

import javax.annotation.PostConstruct;

public class GeneratorService {
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
    }

    public String generatePassword(int length) {
        return generator.generate(length);
    }

    public String generatePassword() {
        return generator.generate(passLength);
    }
}
