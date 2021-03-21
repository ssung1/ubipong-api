package com.eatsleeppong.ubipong;

import com.eatsleeppong.ubipong.tournamentmanager.entity.SpringJpaEvent;
import com.eatsleeppong.ubipong.tournamentmanager.entity.SpringJpaTournament;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RestServiceConfiguration {
    private String allowedOrigins;

    public RestServiceConfiguration(@Value("${allowedOrigins:*}") String allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    /**
     * this one handles all the Spring Data Rest services, annotated
     * with @RepositoryRestResource
     */
    @Bean
    public RepositoryRestConfigurer repositoryRestConfigurer() {
        return new RepositoryRestConfigurer() {
            @Override
            public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry corsRegistry) {
                corsRegistry.addMapping("/**").allowedOriginPatterns(allowedOrigins);
                config.setReturnBodyOnCreate(true)
                    .exposeIdsFor(
                        SpringJpaTournament.class,
                        SpringJpaEvent.class
                    );
            }
        };
    }

    /**
     * this one handles all the plain rest services, annotated with
     * @RestController
     */
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOriginPatterns(allowedOrigins);
            }
        };
    }
}
