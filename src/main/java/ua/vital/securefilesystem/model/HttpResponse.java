package ua.vital.securefilesystem.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class HttpResponse {
    private LocalDateTime timestamp;
    private Integer statusCode;
    private String errorMessage;
    private String httpStatus;
    private String message;
}
