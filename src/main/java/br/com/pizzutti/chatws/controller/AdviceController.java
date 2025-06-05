package br.com.pizzutti.chatws.controller;

import br.com.pizzutti.chatws.dto.AdviceDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class AdviceController {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<AdviceDto> handleResponseStatusEx(ResponseStatusException ex, HttpServletRequest request) {
        var adviceDto = AdviceDto.builder()
                .timestamp(LocalDateTime.now())
                .status(ex.getStatusCode().value())
                .error(ex.getReason())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(ex.getStatusCode()).body(adviceDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AdviceDto> handleGenericEx(Exception ex, HttpServletRequest request) {
        var adviceDto = AdviceDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(adviceDto);
    }

}
