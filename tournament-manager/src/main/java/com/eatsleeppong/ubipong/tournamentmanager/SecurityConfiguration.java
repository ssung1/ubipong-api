package com.eatsleeppong.ubipong.tournamentmanager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Value("${spring.security.enabled: true}")
    private boolean enabled;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        if (enabled) {
            http.authorizeRequests().antMatchers("/**").authenticated();
        } else {
            http.authorizeRequests().antMatchers("/**").permitAll();
        }
    }
}
