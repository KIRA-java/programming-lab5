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

    private String uid;

    private String operationUid;

    private String systemTime;

    private Codes code;

    private ErrorCodes errorCode;

    private ErrorMessages errorMessage;
}