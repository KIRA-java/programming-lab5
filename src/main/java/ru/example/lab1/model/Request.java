package ru.example.lab1.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Request {

    /** Уникальный идентификатор сообщения. */
    @NotBlank(message = "uid обязателен")
    @Size(max = 32, message = "uid должен содержать не более 32 символов")
    private String uid;

    /** Уникальный идентификатор операции. */
    @NotBlank(message = "operationUid обязателен")
    @Size(max = 32,
            message = "operationUid должен содержать не более 32 символов")
    private String operationUid;

    /** Наименование системы отправителя. */
    private Systems systemName;

    /** Время формирования сообщения системой отправителя. */
    @NotBlank(message = "systemTime обязателен")
    private String systemTime;

    /** Источник сообщения. */
    private String source;

    /** Должность сотрудника. */
    @NotNull(message = "position обязателен")
    private Positions position;

    /** Заработная плата сотрудника. */
    @NotNull(message = "salary обязателен")
    @PositiveOrZero(message = "salary не может быть отрицательным")
    private Double salary;

    /** Премиальный множитель, например 2.0. */
    @NotNull(message = "bonus обязателен")
    @PositiveOrZero(message = "bonus не может быть отрицательным")
    private Double bonus;

    /** Количество отработанных дней за год. */
    @NotNull(message = "workDays обязателен")
    @Min(value = 1, message = "workDays должен быть не менее 1")
    @Max(value = 366, message = "workDays должен быть не более 366")
    private Integer workDays;

    /** Идентификатор коммуникации. */
    @Min(value = 1,
            message = "communicationId должен быть не менее 1")
    @Max(value = 100000,
            message = "communicationId должен быть не более 100000")
    private int communicationId;

    /** Идентификатор шаблона. */
    private int templateId;

    /** Код продукта. */
    private int productCode;

    /** Код SMS. */
    private int smsCode;
}