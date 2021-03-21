package com.eatsleeppong.ubipong.tournamentmanager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Value("${spring.security.enabled: true}")
    private boolean enabled;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .cors() // check cors configuration in RestServiceConfiguration first
            .and().oauth2ResourceServer().jwt(); // use oauth2 JWT authentication
        if (enabled) {
            http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/**").authenticated()
                .anyRequest().permitAll();
        } else {
            http.authorizeRequests().antMatchers("/**").permitAll();
        }
    }
}
