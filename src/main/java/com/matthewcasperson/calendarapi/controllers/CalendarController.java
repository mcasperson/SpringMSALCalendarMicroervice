// src/main/java/com/matthewcasperson/calendarapi/controllers/CalendarController.java

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

    private static final String GRAPH_CALENDAR_ENDPOINT = "https://graph.microsoft.com/v1.0/me/calendar/events";

    @Autowired
    private WebClient webClient;

    @GetMapping(value = "/events", produces = "application/json")
    public String events(@RegisteredOAuth2AuthorizedClient("graph") OAuth2AuthorizedClient graph) {
        return callMicrosoftGraphEndpoint(graph);
    }

    private String callMicrosoftGraphEndpoint(OAuth2AuthorizedClient graph) {
        if (null != graph) {
            System.out.println("\n" + graph.getAccessToken().getTokenValue() + "\n");

            String body = webClient
                    .get()
                    .uri(GRAPH_CALENDAR_ENDPOINT)
                    .attributes(oauth2AuthorizedClient(graph))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return body;
        } else {
            return "Graph request failed.";
        }
    }
}
