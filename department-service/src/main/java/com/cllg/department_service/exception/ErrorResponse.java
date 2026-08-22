package com.cllg.department_service.exception;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private final LocalDateTime timestamp;

    private final int status;

    private final String error;

    private final String message;

    private final String path;
}
