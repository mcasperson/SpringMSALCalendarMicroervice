package com.matthewcasperson.calendarapi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

// https://github.com/Azure-Samples/azure-spring-boot-samples/blob/20910da2b61753d33b3c868e719a940e21117578/aad/azure-spring-boot-starter-active-directory/aad-resource-server-obo/src/main/java/com/azure/spring/sample/aad/configuration/AADSampleConfiguration.java
@Configuration
public class WebClientConfig {
    @Bean
    public static WebClient webClient(OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction function =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(oAuth2AuthorizedClientManager);
        return WebClient.builder()
                .apply(function.oauth2Configuration())
                .build();
    }
}
