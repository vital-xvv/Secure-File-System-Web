package ua.vital.securefilesystem.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class ExceptionResponse {
    private int status;
    private String message;
    private String controller;
    private String service;
    private Instant timestamp;
    private String contextPath;

}