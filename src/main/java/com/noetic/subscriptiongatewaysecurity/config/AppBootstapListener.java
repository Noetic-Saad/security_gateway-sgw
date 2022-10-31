package com.noetic.subscriptiongatewaysecurity.config;

import com.noetic.subscriptiongatewaysecurity.repository.AccesEntityRedisRepository;
import com.noetic.subscriptiongatewaysecurity.service.ConfigurationDataManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class AppBootstapListener implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private ConfigurationDataManagerService configurationDataManagerService;
    @Autowired
    AccesEntityRedisRepository accesEntityRedisRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        configurationDataManagerService.bootstapAndCacheConfigurationData();
        //removeExpiredTokens();
    }

    /*public void removeExpiredTokens(){
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() ->  , 0, 5, TimeUnit.SECONDS);
    }*/
}
