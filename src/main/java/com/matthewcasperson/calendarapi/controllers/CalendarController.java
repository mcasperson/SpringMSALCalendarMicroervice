package com.matthewcasperson.calendarapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;


@RestController
public class CalendarController {

    private static final String GRAPH_ME_ENDPOINT = "https://graph.microsoft.com/v1.0/me";
    private static final String GRAPH_CALENDAR_ENDPOINT = "https://graph.microsoft.com/v1.0/calendar";

    @Autowired
    private WebClient webClient;

    @GetMapping("/calendar")
    public String calendar(@RegisteredOAuth2AuthorizedClient("graph") OAuth2AuthorizedClient graph) {
        return callMicrosoftGraphMeEndpoint(graph);
    }

    // https://github.com/Azure-Samples/azure-spring-boot-samples/blob/20910da2b61753d33b3c868e719a940e21117578/aad/azure-spring-boot-starter-active-directory/aad-resource-server-obo/src/main/java/com/azure/spring/sample/aad/controller/SampleController.java
    private String callMicrosoftGraphMeEndpoint(OAuth2AuthorizedClient graph) {
        System.out.println(graph.getAccessToken().getTokenValue());
        if (null != graph) {
            String body = webClient
                    .get()
                    .uri(GRAPH_CALENDAR_ENDPOINT)
                    .attributes(oauth2AuthorizedClient(graph))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return body;
        } else {
            return "Graph response failed.";
        }
    }
}
