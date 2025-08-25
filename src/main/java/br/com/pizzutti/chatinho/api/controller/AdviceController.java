package br.com.pizzutti.chatinho.api.controller;

import br.com.pizzutti.chatinho.api.infra.config.communication.AdviceDto;
import br.com.pizzutti.chatinho.api.infra.config.communication.AdviceEnum;
import br.com.pizzutti.chatinho.api.infra.service.TimeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class AdviceController {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<AdviceDto> handleResponseStatusEx(ResponseStatusException ex,
                                                            HttpServletRequest request) {
        var adviceDto = AdviceDto.builder()
                .timestamp(TimeService.now())
                .status(ex.getStatusCode().value())
                .errors(List.of(Objects.requireNonNull(ex.getReason())))
                .code(AdviceEnum.fromHttpStatus(ex.getStatusCode().value()))
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(adviceDto.status()).body(adviceDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AdviceDto> handleValidationExceptions(MethodArgumentNotValidException ex,
                                                                HttpServletRequest request) {
        var errors = new ArrayList<String>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.add(error.getDefaultMessage());
        });
        var adviceDto = AdviceDto.builder()
                .timestamp(TimeService.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .errors(errors)
                .code(AdviceEnum.BAD_REQUEST)
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(adviceDto.status()).body(adviceDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AdviceDto> handleGenericEx(Exception ex, HttpServletRequest request) {
        var adviceDto = AdviceDto.builder()
                .timestamp(TimeService.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .errors(List.of(ex.getMessage()))
                .code(AdviceEnum.BAD_REQUEST)
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(adviceDto.status()).body(adviceDto);
    }

}
