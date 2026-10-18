package ru.example.lab1.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    /** Уникальный идентификатор сообщения. */
    private String uid;

    /** Уникальный идентификатор операции. */
    private String operationUid;

    /** Время формирования ответа. */
    private String systemTime;

    /** Результат обработки запроса. */
    private Codes code;

    /** Рассчитанная годовая премия. */
    private Double annualBonus;

    /** Рассчитанная квартальная премия. */
    private Double quarterlyBonus;

    /** Код ошибки обработки запроса. */
    private ErrorCodes errorCode;

    /** Описание ошибки обработки запроса. */
    private ErrorMessages errorMessage;
}