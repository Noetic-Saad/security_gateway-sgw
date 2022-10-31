package com.noetic.subscriptiongatewaysecurity.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

public class VendorAccountAccessEntity implements Serializable {
    private long id;
    private String accessToken;
    private Timestamp accessTokenCdatetime;
    private Timestamp accessTokenExpirytime;
    private Integer vendorPlanId;
    private String authentication;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Timestamp getAccessTokenCdatetime() {
        return accessTokenCdatetime;
    }

    public void setAccessTokenCdatetime(Timestamp accessTokenCdatetime) {
        this.accessTokenCdatetime = accessTokenCdatetime;
    }

    public Timestamp getAccessTokenExpirytime() {
        return accessTokenExpirytime;
    }

    public void setAccessTokenExpirytime(Timestamp accessTokenExpirytime) {
        this.accessTokenExpirytime = accessTokenExpirytime;
    }

    public Integer getVendorPlanId() {
        return vendorPlanId;
    }

    public void setVendorPlanId(Integer vendorPlanId) {
        this.vendorPlanId = vendorPlanId;
    }

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VendorAccountAccessEntity that = (VendorAccountAccessEntity) o;
        return id == that.id &&
                Objects.equals(accessToken, that.accessToken) &&
                Objects.equals(accessTokenCdatetime, that.accessTokenCdatetime) &&
                Objects.equals(accessTokenExpirytime, that.accessTokenExpirytime) &&
                Objects.equals(vendorPlanId, that.vendorPlanId) &&
                Objects.equals(authentication, that.authentication);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accessToken, accessTokenCdatetime, accessTokenExpirytime, vendorPlanId, authentication);
    }
}
