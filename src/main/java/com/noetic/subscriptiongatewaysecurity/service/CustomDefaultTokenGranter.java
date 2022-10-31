package com.noetic.subscriptiongatewaysecurity.service;

import com.noetic.subscriptiongatewaysecurity.entities.Client;
import com.noetic.subscriptiongatewaysecurity.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomDefaultTokenGranter implements TokenGranter {

    @Autowired
    ClientDetailsService clientDetailsService;
    OAuth2RequestFactory requestFactory;
    @Autowired
    CustomDefaultTokenService tokenService;
    @Autowired
    ClientRepository clientRepository;
    private Map<String, String> requestParameters = Collections.unmodifiableMap(new HashMap());
    Set<String> scope = new HashSet<String>();


    public CustomDefaultTokenGranter(ClientDetailsService clientDetailsService,CustomDefaultTokenService tokenService) {
        this.clientDetailsService = clientDetailsService;
        this.tokenService = tokenService;
    }
    public CustomDefaultTokenGranter(){}
    @Override
    public OAuth2AccessToken grant(String s, TokenRequest tokenRequest) {
        ClientDetails client = clientDetailsService.loadClientByClientId(tokenRequest.getClientId());
        Client client1 = clientRepository.findByClientId(client.getClientId());
        this.validateGrantType(s,client);
        OAuth2AccessToken token = tokenService.createAccessToken(this.getOAuth2Authentication(client,tokenRequest),client1.getPlanId());

        return token;
    }
    protected void validateGrantType(String grantType, ClientDetails clientDetails) {
        Collection<String> authorizedGrantTypes = clientDetails.getAuthorizedGrantTypes();
        if (authorizedGrantTypes != null && !authorizedGrantTypes.isEmpty() && !authorizedGrantTypes.contains(grantType)) {
            throw new InvalidClientException("Unauthorized grant type: " + grantType);
        }
    }
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        OAuth2Request storedOAuth2Request = this.createOAuth2Request(client);
        return new OAuth2Authentication(storedOAuth2Request, (Authentication)null);
    }
    public OAuth2Request createOAuth2Request(ClientDetails client) {
        HashMap<String, String> modifiable = new HashMap(requestParameters);
        modifiable.remove("password");
        modifiable.remove("client_secret");
        modifiable.put("grant_type", "client_credentials");
        scope.add("read");
        return new OAuth2Request(modifiable, client.getClientId(), client.getAuthorities(), true, scope, client.getResourceIds(), (String)null, (Set)null, (Map)null);
    }
}
