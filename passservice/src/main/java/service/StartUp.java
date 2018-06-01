package service;

import domain.Password;
import util.PassUpdater;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Startup
@Singleton
public class StartUp {
    @Inject
    PasswordService passwordService;

    @PostConstruct
    public void startup() {
        passwordService.setPassword(new Password("willemfacebook", "facebook", "willem"));
        passwordService.setPassword(new Password("willemtwitter", "twitter", "willem"));
        passwordService.setPassword(new Password("willemfontys", "fontys", "willem"));

        passwordService.setPassword(new Password("jorisfacebook", "facebook", "joris"));
        passwordService.setPassword(new Password("joristwitter", "twitter", "joris"));
        passwordService.setPassword(new Password("jorissuma", "suma", "joris"));

    }
}
