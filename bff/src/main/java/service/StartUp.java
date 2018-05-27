package service;


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
    public void startup(){

    }
}
