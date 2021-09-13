package com.matthewcasperson.calendarapi.controllers;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CalendarController {
    @GetMapping("/calendar")
    public String calendar(@RegisteredOAuth2AuthorizedClient("graph") OAuth2AuthorizedClient graph) {
        return graph == null ? "null" : "not null";
    }
}
