package com.noetic.subscriptiongatewaysecurity.service;

import com.noetic.subscriptiongatewaysecurity.entities.VendorAccountAccessEntity;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

@Service
public class CustomDefaultTokenService {

    private SecretKeySpec secretKey ;
    private  byte[] key=null;
    private String secret = "po90ki8u76gt";
    @Autowired
    private final AccessTokenService accessTokenService;
    Date expiration;
    @Autowired
    private ClientService clientDetailsService;
    private int accessTokenValiditySeconds=43200;
    public CustomDefaultTokenService(AccessTokenService accessTokenService) {
        this.accessTokenService = accessTokenService;
    }

    @Transactional
    public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication, Integer planId) throws AuthenticationException {
        /*List<VendorAccountAccessEntity> accesses = this.accessTokenService.getToken(authentication);

        if (accesses != null) {
            for (int i = 0; i < accesses.size(); i++) {
                VendorAccountAccessEntity accountAccess = accesses.remove(i);
                if (isExpired(accountAccess.getAccessTokenExpirytime())) {
                    //this.accessTokenService.removeAccessToken(accountAccess.getAccessToken());
                }
            }

        }*/
        String rand = RandomStringUtils.randomAlphabetic(10)+":"+planId;
        OAuth2AccessToken accessToken = this.createAccessToken2(authentication,rand);
        this.accessTokenService.storeToken(rand,accessToken.getExpiration(), authentication);
        return accessToken;
    }

    public OAuth2AccessToken createAccessToken2(OAuth2Authentication authentication,String random) {
        ObjectCrypter objectCrypter = new ObjectCrypter();
        DefaultOAuth2AccessToken token = null;
        try {
            //token = new DefaultOAuth2AccessToken(objectCrypter.encrypt(secret,random));
            token = new DefaultOAuth2AccessToken(random);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int validitySeconds = this.getAccessTokenValiditySeconds(authentication.getOAuth2Request());
        if (validitySeconds > 0) {
            token.setExpiration(new Date(System.currentTimeMillis() + (long)validitySeconds * 1000L));
        }
        return token;
    }


    public boolean isExpired(Date expiration) {
        return expiration != null && expiration.before(new Date());
    }
    protected int getAccessTokenValiditySeconds(OAuth2Request clientAuth) {
        if (this.clientDetailsService != null) {
            ClientDetails client = this.clientDetailsService.loadClientByClientId(clientAuth.getClientId());
            Integer validity = client.getAccessTokenValiditySeconds();
            if (validity != null) {
                return validity;
            }
        }

        return this.accessTokenValiditySeconds;
    }
    public  void setKey(String myKey)
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public  String encrypt(String strToEncrypt, String secret)
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        }
        catch (Exception e)
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }
}
