package com.edu.utez.bibliotecadigital.controller;

import com.edu.utez.bibliotecadigital.controller.dto.LoanRequest;
import com.edu.utez.bibliotecadigital.controller.dto.LoanResponse;
import com.edu.utez.bibliotecadigital.controller.dto.LoanStatusResponse;
import com.edu.utez.bibliotecadigital.service.BooksLoanService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/loans")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class LoansController {

    private final BooksLoanService booksLoanService;

    @PostMapping
    public ResponseEntity<@NonNull LoanResponse> createLoan(@Valid @RequestBody LoanRequest request){
        LoanResponse loan = booksLoanService.createLoan(request);
        if(loan.status().equals(LoanStatusResponse.GRANTED)){

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/user/{id}")
                    .buildAndExpand(loan.user().id())
                    .toUri();
            return ResponseEntity.created(location)
                    .body(loan);
        }

        return ResponseEntity.accepted()
                .body(loan);

    }

    @PostMapping("/{id}/return")
    public LoanResponse returnLoan(@PathVariable UUID id){
        return booksLoanService.returnLoaned(id);
    }

    @GetMapping("/active")
    public List<LoanResponse> getActiveLoans(){
        return booksLoanService.findAll();
    }

    @GetMapping("/user/{id}")
    public List<LoanResponse> getLoansForUser(@PathVariable UUID id){
        return booksLoanService.findLoansForUser(id);
    }
}
