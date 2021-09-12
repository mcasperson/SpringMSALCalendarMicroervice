package com.matthewcasperson.calendarapi.controllers;

import com.matthewcasperson.calendarapi.components.OboAuthProvider;
import com.microsoft.graph.models.User;
import com.microsoft.graph.requests.GraphServiceClient;
import okhttp3.Request;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
public class CalendarController {
    private OboAuthProvider oboAuthProvider;

    public CalendarController(OboAuthProvider oboAuthProvider) {
        this.oboAuthProvider = oboAuthProvider;
    }

    @GetMapping("/calendar")
    public String calendar() {
        GraphServiceClient<Request> graphClient = GraphServiceClient
                .builder()
                .authenticationProvider(oboAuthProvider)
                .buildClient();

        User user = graphClient.me().buildRequest().get();
        return user.id;
    }


}
