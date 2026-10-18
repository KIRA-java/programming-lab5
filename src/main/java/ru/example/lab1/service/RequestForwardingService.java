package ru.example.lab1.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.example.lab1.model.Request;

@Slf4j
@Service
public class RequestForwardingService {

    private final RestTemplate restTemplate;

    public RequestForwardingService() {
        SimpleClientHttpRequestFactory factory =
                new SimpleClientHttpRequestFactory();

        factory.setConnectTimeout(3000);
        factory.setReadTimeout(5000);

        restTemplate = new RestTemplate(factory);
    }

    public void forward(Request request, long receivedAt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(
                "X-Service1-Received-At",
                Long.toString(receivedAt)
        );

        HttpEntity<Request> entity = new HttpEntity<>(request, headers);

        log.info("Отправка запроса в Сервис 2: {}", request);

        ResponseEntity<String> result = restTemplate.postForEntity(
                "http://localhost:8084/feedback",
                entity,
                String.class
        );

        log.info(
                "Получен ответ от Сервиса 2, HTTP {}",
                result.getStatusCode().value()
        );
    }
}