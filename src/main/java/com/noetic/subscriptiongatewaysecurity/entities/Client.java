package com.noetic.subscriptiongatewaysecurity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "vendor_plan_accounts")
public class Client {

    @Id
    @Column(name="id")
    private Integer id;
    @Column(name="plan_id")
    private Integer planId;
    @Column(name="client_id")
    private String clientId;
    @Column(name="client_secret")
    private String clientSecret;
    @Column(name="status")
    private int status;
    @Column(name="response_type_id")
    private int responseTypeId;
    @Column(name="token_validity")
    private Integer tokenValidity;

    public
    Integer getId() {
        return id;
    }

    public
    void setId(Integer id) {
        this.id = id;
    }

    public
    Integer getPlanId() {
        return planId;
    }

    public
    void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public
    String getClientId() {
        return clientId;
    }

    public
    void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public
    String getClientSecret() {
        return clientSecret;
    }

    public
    void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public
    int getStatus() {
        return status;
    }

    public
    void setStatus(int status) {
        this.status = status;
    }


    public
    int getResponseTypeId() {
        return responseTypeId;
    }

    public
    void setResponseTypeId(int responseTypeId) {
        this.responseTypeId = responseTypeId;
    }

    public
    Integer getTokenValidity() {
        return tokenValidity;
    }

    public
    void setTokenValidity(Integer tokenValidity) {
        this.tokenValidity = tokenValidity;
    }


    public Client(){}
}
