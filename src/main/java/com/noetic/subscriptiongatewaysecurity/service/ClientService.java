package com.noetic.subscriptiongatewaysecurity.service;

import com.noetic.subscriptiongatewaysecurity.entities.Client;
import com.noetic.subscriptiongatewaysecurity.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public
class ClientService implements ClientDetailsService {

    private Set<String> authorities = new HashSet<>(Arrays.asList("client_credentials", "authorization_code", "refresh_token", "implicit"));
    private Set<String> scopes = new HashSet<>(Arrays.asList("read","write","trust"));

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ConfigurationDataManagerService dataManagerService;

    public
    ClientService(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    private LoginAttemptService loginAttemptService;

    @Override
    public
    ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        if (loginAttemptService.isBlocked(clientId)) {
            throw new RuntimeException("blocked");
        }else {
            Client client = dataManagerService.getVendorPlanAccount(clientId);
            if(client==null){
                loginAttemptService.loginFailed(clientId);
                throw new UsernameNotFoundException("Invalid username");
            }else {
                loginAttemptService.loginSucceeded(clientId);
                DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
                BaseClientDetails base = new BaseClientDetails();
                base.setClientId(client.getClientId());
                base.setClientSecret(client.getClientSecret());
                base.setAccessTokenValiditySeconds(client.getTokenValidity());
                base.setAuthorizedGrantTypes(authorities);
                base.setScope(scopes);
                return base;
            }
        }
    }
}

