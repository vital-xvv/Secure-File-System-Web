package ua.vital.securefilesystem.advice;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import ua.vital.securefilesystem.exception.ExceptionResponse;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleInvalidArgumentException(MethodArgumentNotValidException e){
        Map<String, String> errorMap = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
            errorMap.put(error.getField(), error.getDefaultMessage()));
        errorMap.put("status", String.valueOf(HttpStatus.BAD_REQUEST.value()));
        errorMap.put("http_message", HttpStatus.BAD_REQUEST.toString());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityNotFoundException.class)
    public Map<String, String> noEntityPresentHandler(EntityNotFoundException e){
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error_message", e.getMessage());
        errorMap.put("status", String.valueOf(HttpStatus.BAD_REQUEST.value()));
        errorMap.put("http_message", HttpStatus.BAD_REQUEST.toString());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerGeneral(Exception ex, HandlerMethod handlerMethod, HttpServletRequest request){
        Class<?> controllerName = handlerMethod.getMethod().getDeclaringClass();
        String methodName = handlerMethod.getMethod().getName();
        ExceptionResponse details = new ExceptionResponse();

        details.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        details.setMessage(ex.getMessage());
        details.setController(controllerName.toString());
        details.setService(methodName);
        details.setTimestamp(Instant.now());
        details.setContextPath(request.getRequestURL().toString());
        return ResponseEntity.internalServerError().body(details);
    }
}
