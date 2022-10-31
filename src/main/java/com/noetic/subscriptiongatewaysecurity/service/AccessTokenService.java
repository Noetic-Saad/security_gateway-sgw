package com.noetic.subscriptiongatewaysecurity.service;

import com.noetic.subscriptiongatewaysecurity.config.SerializableObjectConverter;
import com.noetic.subscriptiongatewaysecurity.entities.Client;
import com.noetic.subscriptiongatewaysecurity.entities.VendorAccountAccessEntity;
import com.noetic.subscriptiongatewaysecurity.repository.AccesEntityRedisRepository;
//import com.noetic.subscriptiongatewaysecurity.repository.AccessTokenRepository;
import com.noetic.subscriptiongatewaysecurity.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public
class AccessTokenService implements TokenStore {
   /* @Autowired
    AccessTokenRepository accessTokenRepository;*/
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    AccesEntityRedisRepository accesEntityRedisRepository;

    @Autowired
    ConfigurationDataManagerService dataManagerService;


    /*public
    AccessTokenService(AccessTokenRepository accessTokenRepository) {
        this.accessTokenRepository = accessTokenRepository;
    }*/

    public
    AccessTokenService() {
    }

    @Override
    public
    OAuth2Authentication readAuthentication(OAuth2AccessToken oAuth2AccessToken) {
        VendorAccountAccessEntity vpAccountAccess=accesEntityRedisRepository.findByAccessToken(oAuth2AccessToken.getValue());
        return SerializableObjectConverter.deserialize(vpAccountAccess.getAuthentication());
    }

    public
    VendorAccountAccessEntity getAccesAccount(OAuth2AccessToken oAuth2AccessToken) {
        VendorAccountAccessEntity vpAccountAccess=accesEntityRedisRepository.findByAccessToken(oAuth2AccessToken.getValue());
        return vpAccountAccess;
    }

    @Override
    public
    OAuth2Authentication readAuthentication(String s) {
        return null;
    }

    @Override
    public
    void storeAccessToken(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        VendorAccountAccessEntity vpAccountAccess = new VendorAccountAccessEntity();
        Client client = new Client();
        //client = clientRepository.findByClientId(oAuth2Authentication.getOAuth2Request().getClientId());
        client = dataManagerService.getVendorPlanAccount(oAuth2Authentication.getOAuth2Request().getClientId());
        String token = oAuth2AccessToken.getValue();
        String accessToken = null;
        String authentication = Base64.getEncoder().encodeToString((oAuth2Authentication.getOAuth2Request().getClientId()+":"+client.getClientSecret()).getBytes());
        vpAccountAccess.setVendorPlanId(client.getPlanId());
        vpAccountAccess.setAccessToken(oAuth2AccessToken.toString());
        vpAccountAccess.setAccessTokenExpirytime(new Timestamp(oAuth2AccessToken.getExpiration().getTime()));
        vpAccountAccess.setAccessTokenCdatetime(new Timestamp(new Date().getTime()));
        vpAccountAccess.setAuthentication(authentication);
        VendorAccountAccessEntity var  = accesEntityRedisRepository.findByAccessToken(token);
        if(var==null) {
            accesEntityRedisRepository.save(vpAccountAccess);
            return;
        }
        if(var.getAccessToken().equals(token)){
            return;
        }
        accesEntityRedisRepository.save(vpAccountAccess);
    }

    public
    void storeToken(String token,Date expiry, OAuth2Authentication oAuth2Authentication) {
        VendorAccountAccessEntity vpAccountAccess = new VendorAccountAccessEntity();
        Client client = new Client();
        //client = clientRepository.findByClientId(oAuth2Authentication.getOAuth2Request().getClientId());
        client = dataManagerService.getVendorPlanAccount(oAuth2Authentication.getOAuth2Request().getClientId());
        String accessToken = null;
        String authentication = Base64.getEncoder().encodeToString((oAuth2Authentication.getOAuth2Request().getClientId()+":"+client.getClientSecret()).getBytes());
        vpAccountAccess.setVendorPlanId(client.getPlanId());
        vpAccountAccess.setAccessToken(token);
        vpAccountAccess.setAccessTokenExpirytime(new Timestamp(expiry.getTime()));
        vpAccountAccess.setAccessTokenCdatetime(new Timestamp(new Date().getTime()));
        vpAccountAccess.setAuthentication(authentication);
        VendorAccountAccessEntity var  = accesEntityRedisRepository.findByAccessToken(token);
        if(var==null) {
            accesEntityRedisRepository.save(vpAccountAccess);
            return;
        }
        if(var.getAccessToken().equals(token)){
            return;
        }
        accesEntityRedisRepository.save(vpAccountAccess);
    }

    @Override
    public
    OAuth2AccessToken readAccessToken(String s) {
        VendorAccountAccessEntity vpAccountAccess= accesEntityRedisRepository.findByAccessToken(s);
        if(vpAccountAccess!=null){
        DefaultOAuth2AccessToken defaultOAuth2AccessToken= new DefaultOAuth2AccessToken(vpAccountAccess.getAccessToken());
            defaultOAuth2AccessToken.setExpiration(new Timestamp(vpAccountAccess.getAccessTokenExpirytime().getTime()));
            return defaultOAuth2AccessToken;
        }
        return null;
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken oAuth2AccessToken) {
        String token = oAuth2AccessToken.getValue();

        accesEntityRedisRepository.deleteByAccessToken(token);
    }

    public
    VendorAccountAccessEntity findByToken(String s) {
        VendorAccountAccessEntity vpAccountAccess= null;//accessTokenRepository.findByAccessToken(s);
        return vpAccountAccess;
    }
    public
    void removeAccessToken(String oAuth2AccessToken) {
        accesEntityRedisRepository.deleteByAccessToken(oAuth2AccessToken);
        System.out.println("Deleted");
    }

    @Override
    public
    void storeRefreshToken(OAuth2RefreshToken oAuth2RefreshToken, OAuth2Authentication oAuth2Authentication) {

    }

    @Override
    public
    OAuth2RefreshToken readRefreshToken(String s) {
        return null;
    }

    @Override
    public
    OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken oAuth2RefreshToken) {
        return null;
    }

    @Override
    public
    void removeRefreshToken(OAuth2RefreshToken oAuth2RefreshToken) {

    }

    @Override
    public
    void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken oAuth2RefreshToken) {

    }

    /*public List<VendorAccountAccessEntity> getToken(OAuth2Authentication oAuth2Authentication){
        Integer planId = clientRepository.findByClientId(oAuth2Authentication.getOAuth2Request().getClientId()).getPlanId();
        return accessTokenRepository.findByVendorPlanId(planId);
    }*/

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication oAuth2Authentication) {
        /*OAuth2AccessToken OAuth2AccessToken = null;
        String token= null;
        VendorAccountAccessEntity accessToken = accessTokenRepository.findAccessTokenByVendorPlanId(clientRepository.findByClientId(oAuth2Authentication.getOAuth2Request().getClientId()).getId());

        if(accessToken==null){
            return null;
        }
        DefaultOAuth2AccessToken defaultOAuth2AccessToken = new DefaultOAuth2AccessToken(accessToken.getAccessToken());
        defaultOAuth2AccessToken.setExpiration(accessToken.getAccessTokenExpirytime());*/
        DefaultOAuth2AccessToken defaultOAuth2AccessToken = null;
        return defaultOAuth2AccessToken;
    }

    @Override
    public
    Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String s, String s1) {
        return null;
    }

    @Override
    public
    Collection<OAuth2AccessToken> findTokensByClientId(String s) {
        return null;
    }

    protected OAuth2AccessToken deserializeAccessToken(byte[] token) {
        return (OAuth2AccessToken) SerializationUtils.deserialize(token);
    }


}
