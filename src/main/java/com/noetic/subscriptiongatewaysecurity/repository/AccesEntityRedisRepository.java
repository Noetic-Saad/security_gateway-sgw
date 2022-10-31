package com.noetic.subscriptiongatewaysecurity.repository;

import com.noetic.subscriptiongatewaysecurity.entities.VendorAccountAccessEntity;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AccesEntityRedisRepository {

    private HashOperations hashOperations;

    private RedisTemplate redisTemplate;

    public AccesEntityRedisRepository(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
        this.hashOperations = this.redisTemplate.opsForHash();
    }

    public void save(VendorAccountAccessEntity entity){
        hashOperations.put("ACCES_TOKEN", entity.getAccessToken(), entity);
    }

    public List findAll(){
        return hashOperations.values("ACCES_TOKEN");
    }

    public VendorAccountAccessEntity findByAccessToken(String token){
        return (VendorAccountAccessEntity) hashOperations.get("ACCES_TOKEN", token);
    }

    public void update(VendorAccountAccessEntity entity){
        save(entity);
    }

    public void deleteByAccessToken(String token){
        hashOperations.delete("ACCES_TOKEN", token);
    }




}
