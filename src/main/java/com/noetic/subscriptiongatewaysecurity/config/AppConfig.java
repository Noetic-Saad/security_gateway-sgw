package com.noetic.subscriptiongatewaysecurity.config;

import com.noetic.subscriptiongatewaysecurity.service.AccessTokenService;
import com.noetic.subscriptiongatewaysecurity.service.CustomDefaultTokenGranter;
import com.noetic.subscriptiongatewaysecurity.service.CustomDefaultTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.sql.DataSource;

/**
 * Created by daz on 01/07/2017.
 */
@Configuration
public class AppConfig {

    ClientDetailsService clientDetailsService;
    CustomDefaultTokenService tokenService;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;
    
    @Value("${spring.datasource.username}")
    private String dbUsername;
    
    @Value("${spring.datasource.password}")
    private String dbPassword;
    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setUrl(datasourceUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        
        return dataSource;
    }
    
    @Bean
    public
    TokenStore tokenStore() {
        return new AccessTokenService();
    }

    @Bean
    public TokenGranter tokenServices(){
        return new CustomDefaultTokenGranter();
    }
}
