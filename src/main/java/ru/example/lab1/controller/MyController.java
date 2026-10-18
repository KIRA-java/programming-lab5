package ru.example.lab1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.example.lab1.model.Request;
import ru.example.lab1.model.Response;
import ru.example.lab1.service.FeedbackService;

@RestController
@RequiredArgsConstructor
public class MyController {

    private final FeedbackService feedbackService;

    @PostMapping("/feedback")
    public ResponseEntity<Response> feedback(
            @RequestBody Request request
    ) {
        return feedbackService.process(request);
    }
}