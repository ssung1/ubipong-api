package com.eatsleeppong.ubipong.tournamentmanager;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

/**
 * This contains the necessary Spring configuration, initialized either
 * directly as @Bean methods or through @ComponentScan.  We keep these
 * as @TestConfiguration so that these beans would not pollute tests in
 * other subprojects when running from VSCode.
 * 
 * When running through Gradle or IntelliJ, it is sufficient to annotate
 * this class as @Configuration.
 */
@TestConfiguration
@ComponentScan
@EnableAutoConfiguration
public class AdapterTestConfiguration {
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
