package ru.example.lab1.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.example.lab1.exception.UnsupportedCodeException;
import ru.example.lab1.exception.ValidationFailedException;
import ru.example.lab1.model.Codes;
import ru.example.lab1.model.ErrorCodes;
import ru.example.lab1.model.ErrorMessages;
import ru.example.lab1.model.Request;
import ru.example.lab1.model.Response;
import ru.example.lab1.service.ModifyResponseService;
import ru.example.lab1.service.ValidationService;
import ru.example.lab1.util.DateTimeUtil;

import java.util.Date;

@Slf4j
@RestController
public class MyController {

    private final ValidationService validationService;
    private final ModifyResponseService operationUidService;
    private final ModifyResponseService systemTimeService;

    public MyController(
            ValidationService validationService,
            @Qualifier("ModifyOperationUidResponseService")
            ModifyResponseService operationUidService,
            @Qualifier("ModifySystemTimeResponseService")
            ModifyResponseService systemTimeService) {

        this.validationService = validationService;
        this.operationUidService = operationUidService;
        this.systemTimeService = systemTimeService;
    }

    @PostMapping("/feedback")
    public ResponseEntity<Response> feedback(
            @RequestBody Request request) {

        log.info("Получен запрос: {}", request);

        Response response = Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(
                        DateTimeUtil.getCustomFormat().format(new Date())
                )
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();

        log.info("Сформирован исходный ответ: {}", response);

        try {
            validationService.validate(request);
            log.info("Запрос прошёл валидацию, uid={}", request.getUid());

            response = operationUidService.modify(response);
            response = systemTimeService.modify(response);

            log.info("Отправлен ответ HTTP 200: {}", response);

            return ResponseEntity.ok(response);

        } catch (ValidationFailedException exception) {
            log.error("Ошибка валидации, uid={}: {}",
                    request.getUid(), exception.getMessage());

            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.VALIDATION_EXCEPTION);
            response.setErrorMessage(ErrorMessages.VALIDATION);

            log.info("Ответ изменён. Отправлен HTTP 400: {}", response);

            return ResponseEntity.badRequest().body(response);

        } catch (UnsupportedCodeException exception) {
            log.error("Неподдерживаемый код, uid={}: {}",
                    request.getUid(), exception.getMessage());

            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNSUPPORTED_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNSUPPORTED);

            log.info("Ответ изменён. Отправлен HTTP 400: {}", response);

            return ResponseEntity.badRequest().body(response);

        } catch (Exception exception) {
            log.error("Непредвиденная ошибка, uid="
                    + request.getUid(), exception);

            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNKNOWN);

            log.info("Ответ изменён. Отправлен HTTP 500: {}", response);

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }
}