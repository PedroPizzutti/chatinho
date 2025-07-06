package br.com.pizzutti.chatws.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Status")
@RestController
@RequestMapping("v1/status")
public class StatusController {
    @GetMapping
    @Operation(summary = "Verifica o status do serviço")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok("ON-LINE");
    }
}
