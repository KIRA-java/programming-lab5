package ru.example.lab1.service;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import ru.example.lab1.model.Request;

@Service
@Order(2)
public class ModifySourceRequestService implements ModifyRequestService {

    @Override
    public void modify(Request request) {
        request.setSource("Service 1");
    }
}