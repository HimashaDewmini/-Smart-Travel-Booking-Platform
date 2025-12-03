package com.example.PaymentService.Controller;

import com.example.PaymentService.DTO.PaymentRequest;
import com.example.PaymentService.DTO.PaymentResponse;
import com.example.PaymentService.Service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> processPayment(@RequestBody PaymentRequest request) {
        return ResponseEntity.ok(service.processPayment(request));
    }
}
