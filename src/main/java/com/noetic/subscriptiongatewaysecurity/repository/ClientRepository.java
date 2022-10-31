package com.noetic.subscriptiongatewaysecurity.repository;

import com.noetic.subscriptiongatewaysecurity.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    Client findByClientId(String clientId);
}

