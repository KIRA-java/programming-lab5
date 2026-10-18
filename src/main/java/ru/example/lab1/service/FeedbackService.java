package ru.example.lab1.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.example.lab1.exception.UnsupportedCodeException;
import ru.example.lab1.exception.ValidationFailedException;
import ru.example.lab1.model.Codes;
import ru.example.lab1.model.ErrorCodes;
import ru.example.lab1.model.ErrorMessages;
import ru.example.lab1.model.Request;
import ru.example.lab1.model.Response;
import ru.example.lab1.util.DateTimeUtil;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class FeedbackService {

    private final ValidationService validationService;
    private final ModifyResponseService operationUidService;
    private final ModifyResponseService systemTimeService;
    private final List<ModifyRequestService> requestModifiers;
    private final RequestForwardingService forwardingService;
    private final AnnualBonusService annualBonusService;

    public FeedbackService(
            ValidationService validationService,
            @Qualifier("ModifyOperationUidResponseService")
            ModifyResponseService operationUidService,
            @Qualifier("ModifySystemTimeResponseService")
            ModifyResponseService systemTimeService,
            List<ModifyRequestService> requestModifiers,
            RequestForwardingService forwardingService,
            AnnualBonusService annualBonusService
    ) {
        this.validationService = validationService;
        this.operationUidService = operationUidService;
        this.systemTimeService = systemTimeService;
        this.requestModifiers = requestModifiers;
        this.forwardingService = forwardingService;
        this.annualBonusService = annualBonusService;
    }

    public ResponseEntity<Response> process(Request request) {
        long receivedAt = System.currentTimeMillis();

        log.info("Сервис 1 получил запрос: {}", request);

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

        try {
            validationService.validate(request);

            response = operationUidService.modify(response);
            response = systemTimeService.modify(response);

            double annualBonus = annualBonusService.calculate(
                    request.getPosition(),
                    request.getSalary(),
                    request.getBonus(),
                    request.getWorkDays()
            );

            double quarterlyBonus = 0.0;

            if (request.getPosition().isManager()) {
                quarterlyBonus = annualBonusService.calculateQuarterly(
                        request.getPosition(),
                        request.getSalary(),
                        request.getBonus(),
                        request.getWorkDays()
                );
            }

            response.setAnnualBonus(annualBonus);
            response.setQuarterlyBonus(quarterlyBonus);

            for (ModifyRequestService modifier : requestModifiers) {
                modifier.modify(request);
            }

            log.info("Изменённый запрос: {}", request);

            forwardingService.forward(request, receivedAt);

            log.info("Отправлен ответ HTTP 200: {}", response);

            return ResponseEntity.ok(response);

        } catch (ValidationFailedException exception) {
            log.warn("Ошибка валидации: {}", exception.getMessage());

            return failure(
                    response,
                    HttpStatus.BAD_REQUEST,
                    ErrorCodes.VALIDATION_EXCEPTION,
                    ErrorMessages.VALIDATION
            );

        } catch (UnsupportedCodeException exception) {
            log.warn("Неподдерживаемый код: {}", exception.getMessage());

            return failure(
                    response,
                    HttpStatus.BAD_REQUEST,
                    ErrorCodes.UNSUPPORTED_EXCEPTION,
                    ErrorMessages.UNSUPPORTED
            );

        } catch (IllegalArgumentException exception) {
            log.warn("Ошибка данных для расчёта: {}", exception.getMessage());

            return failure(
                    response,
                    HttpStatus.BAD_REQUEST,
                    ErrorCodes.VALIDATION_EXCEPTION,
                    ErrorMessages.VALIDATION
            );

        } catch (Exception exception) {
            log.error("Непредвиденная ошибка", exception);

            return failure(
                    response,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ErrorCodes.UNKNOWN_EXCEPTION,
                    ErrorMessages.UNKNOWN
            );
        }
    }

    private ResponseEntity<Response> failure(
            Response response,
            HttpStatus status,
            ErrorCodes errorCode,
            ErrorMessages errorMessage
    ) {
        response.setCode(Codes.FAILED);
        response.setErrorCode(errorCode);
        response.setErrorMessage(errorMessage);
        response.setAnnualBonus(null);
        response.setQuarterlyBonus(null);

        log.info("Отправлен ответ HTTP {}: {}", status.value(), response);

        return ResponseEntity.status(status).body(response);
    }
}