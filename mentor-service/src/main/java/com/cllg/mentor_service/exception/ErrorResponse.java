package com.cllg.mentor_service.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private final LocalDateTime timesStamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;
}
