package ru.example.lab1.service;

import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import ru.example.lab1.exception.UnsupportedCodeException;
import ru.example.lab1.exception.ValidationFailedException;
import ru.example.lab1.model.Request;

import java.util.stream.Collectors;

@Slf4j
@Service
public class ValidationServiceImpl implements ValidationService {

    private final Validator validator;

    public ValidationServiceImpl(Validator validator) {
        this.validator = validator;
    }

    @Override
    public void validate(Request request) {
        log.info("Начата валидация запроса, uid={}", request.getUid());

        BindingResult bindingResult =
                new BeanPropertyBindingResult(request, "request");

        SpringValidatorAdapter adapter =
                new SpringValidatorAdapter(validator);

        adapter.validate(request, bindingResult);

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> {
                if (error instanceof FieldError fieldError) {
                    log.error("Ошибка поля {}: {}",
                            fieldError.getField(),
                            fieldError.getDefaultMessage());
                } else {
                    log.error("Ошибка валидации: {}",
                            error.getDefaultMessage());
                }
            });

            String message = bindingResult.getAllErrors().stream()
                    .map(error -> {
                        if (error instanceof FieldError fieldError) {
                            return fieldError.getField()
                                    + ": "
                                    + fieldError.getDefaultMessage();
                        }
                        return error.getDefaultMessage();
                    })
                    .sorted()
                    .collect(Collectors.joining("; "));

            log.error("Выбрасывается ValidationFailedException: {}",
                    message);

            throw new ValidationFailedException(message);
        }

        if ("123".equals(request.getUid())) {
            String message = "Значение uid = 123 не поддерживается";

            log.error("Выбрасывается UnsupportedCodeException: {}",
                    message);

            throw new UnsupportedCodeException(message);
        }

        log.info("Валидация завершена успешно, uid={}",
                request.getUid());
    }
}