package com.noetic.subscriptiongatewaysecurity.controller;

import com.noetic.subscriptiongatewaysecurity.oauth.RequestProperties;
import com.noetic.subscriptiongatewaysecurity.oauth.SecurityResponse;
import com.noetic.subscriptiongatewaysecurity.service.AccessTokenService;
import com.noetic.subscriptiongatewaysecurity.service.ObjectCrypter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
public
class TokenController {

    private SecretKeySpec secretKey ;
    private  byte[] key=null;
    private String secret = "po90ki8u76gt";

    @Autowired
    private
    AccessTokenService accessTokenService;
    protected final Log logger = LogFactory.getLog(this.getClass());
    private WebResponseExceptionTranslator exceptionTranslator = new DefaultWebResponseExceptionTranslator();

    public void setExceptionTranslator(WebResponseExceptionTranslator exceptionTranslator) {
        this.exceptionTranslator = exceptionTranslator;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/oauth/validate")
    @ResponseBody
    public
    SecurityResponse checkToken(@RequestBody RequestProperties request) {
        ObjectCrypter objectCrypter = new ObjectCrypter();
        String accessToken = null;
        try {
            String token = request.getToken();
            //System.out.println("Decrypting This = " + token);
            //accessToken = objectCrypter.decrypt(secret,token);
            accessToken = token;
        } catch (Exception e) {
            e.printStackTrace();
        }

        OAuth2AccessToken token = this.accessTokenService.readAccessToken(accessToken);
        if (token == null) {
            throw new InvalidTokenException("Token was not recognised");
        } else if (token.isExpired()) {
            throw new InvalidTokenException("Token has expired");
        } else {
            String arr[] = accessToken.split(":");
            return convertAccessToken(token, arr[1]);
        }
    }

    @ExceptionHandler({InvalidTokenException.class})
    public
    ResponseEntity<OAuth2Exception> handleException(Exception e) throws Exception {
        this.logger.info("Handling error: " + e.getClass().getSimpleName() + ", " + e.getMessage());
        InvalidTokenException e400 = new InvalidTokenException(e.getMessage()) {
            public int getHttpErrorCode() {
                return 400;
            }
        };
        return this.exceptionTranslator.translate(e400);
    }

    private
    SecurityResponse convertAccessToken(OAuth2AccessToken token, String planId) {
        Map<String, Object> response = new HashMap();
        SecurityResponse res = new SecurityResponse();

        if(planId!=null){
            response.put("vendor_plan_id", planId);
            res.setVendorPlanId(planId);
        }

        if (token.getAdditionalInformation().containsKey("jti")) {
            response.put("jti", token.getAdditionalInformation().get("jti"));
        }

        if (token.getExpiration() != null) {
            res.setExpiryTime(token.getExpiration().getTime() / 1000L);
        }

        return res;
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
    public  String decrypt(String strToDecrypt, String secret)
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        }
        catch (Exception e)
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
}

