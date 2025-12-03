package com.edu.utez.bibliotecadigital.controller;

import com.edu.utez.bibliotecadigital.controller.dto.LoanRequest;
import com.edu.utez.bibliotecadigital.controller.dto.LoanResponse;
import com.edu.utez.bibliotecadigital.controller.dto.LoanReturnRequest;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/loans")
public class LoansController {

    @PostMapping
    public ResponseEntity<?> createLoan(@Valid @RequestBody LoanRequest request){
        return null;
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<?> returnLoan(@PathVariable UUID id, @Valid @RequestBody LoanReturnRequest request){
        return null;
    }

    @GetMapping("/active")
    public List<LoanResponse> getActiveLoans(){
        return null;
    }
}
