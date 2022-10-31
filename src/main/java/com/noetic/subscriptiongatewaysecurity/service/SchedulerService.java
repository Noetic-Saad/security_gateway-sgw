package com.noetic.subscriptiongatewaysecurity.service;

import com.noetic.subscriptiongatewaysecurity.entities.VendorAccountAccessEntity;
import com.noetic.subscriptiongatewaysecurity.repository.AccesEntityRedisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@EnableScheduling
public class SchedulerService {

    Logger log = LoggerFactory.getLogger(SchedulerService.class.getName());
    @Autowired
    AccesEntityRedisRepository accesEntityRedisRepository;

    @Scheduled(cron = "0 0/30 * * * ?")
    public void expireRedisKeys(){
  int count=0;
        log.info("SCHDULER STARTED");
        List accesEntityRedisRepositoryAll = accesEntityRedisRepository.findAll();
log.info("Total Size: "+accesEntityRedisRepositoryAll.size() );
        for (int i = 0; i < accesEntityRedisRepositoryAll.size() ; i++) {
            VendorAccountAccessEntity o = (VendorAccountAccessEntity) accesEntityRedisRepositoryAll.get(i);

            if(o.getAccessTokenExpirytime().before(Timestamp.valueOf(LocalDateTime.now().minusHours(1)))){
                accesEntityRedisRepository.deleteByAccessToken(o.getAccessToken());
                log.info(o.getAccessToken()+" --> DELETED");
    count++;
            }

        }
       log.info("Total DELETED : " + count);
    }
}
