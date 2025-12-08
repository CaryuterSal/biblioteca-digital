package com.edu.utez.bibliotecadigital.controller;

import com.edu.utez.bibliotecadigital.controller.dto.HistoryItemResponse;
import com.edu.utez.bibliotecadigital.controller.dto.HistoryResponse;
import com.edu.utez.bibliotecadigital.controller.dto.HistoryUndoRequest;
import com.edu.utez.bibliotecadigital.service.HistoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping
    public List<HistoryResponse> getHistory(Authentication authentication) {
        if(authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))){
            return historyService.getAllHistory();
        }
        return List.of(historyService.getHistoryForCurrent());
    }

    @GetMapping("/users/{id}")
    public HistoryResponse getHistoryByUser(@PathVariable UUID id){
        return historyService.getHistoryForUser(id);
    }

    @PostMapping("/undo")
    public List<HistoryItemResponse> undoAction(@Valid @RequestBody HistoryUndoRequest request){
        return historyService.undoAction(request);
    }
}
