package ru.example.lab1.service;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import ru.example.lab1.model.Request;
import ru.example.lab1.model.Systems;

@Service
@Order(1)
public class ModifySystemNameRequestService implements ModifyRequestService {

    @Override
    public void modify(Request request) {
        request.setSystemName(Systems.CRM);
    }
}