package com.matthewcasperson.calendarapi.controllers;

import com.matthewcasperson.calendarapi.components.OboAuthProvider;
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
    public String calendar() throws ExecutionException, InterruptedException {
        return oboAuthProvider.getAuthorizationTokenAsync(null).get();
    }


}
