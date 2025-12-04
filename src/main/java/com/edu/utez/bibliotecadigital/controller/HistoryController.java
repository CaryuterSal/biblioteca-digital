package com.edu.utez.bibliotecadigital.controller;

import com.edu.utez.bibliotecadigital.controller.dto.HistoryItemResponse;
import com.edu.utez.bibliotecadigital.controller.dto.HistoryResponse;
import com.edu.utez.bibliotecadigital.controller.dto.HistoryUndoRequest;
import com.edu.utez.bibliotecadigital.service.HistoryService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping
    public List<HistoryResponse> getHistory(Authentication authentication){
        return historyService.getAllHistory();
    }

    @GetMapping("/users/{id}")
    public HistoryResponse getHistoryByUser(@PathVariable UUID id){
        return historyService.getHistoryForCurrent(id);
    }

    @PostMapping("/undo")
    public List<HistoryItemResponse> undoAction(@Valid @RequestBody HistoryUndoRequest request){
        return historyService.undoAction(request);
    }
}
