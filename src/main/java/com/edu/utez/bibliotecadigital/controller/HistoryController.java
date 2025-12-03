package com.edu.utez.bibliotecadigital.controller;

import com.edu.utez.bibliotecadigital.controller.dto.HistoryResponse;
import com.edu.utez.bibliotecadigital.controller.dto.HistoryUndoRequest;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/history")
public class HistoryController {

    @GetMapping
    public List<HistoryResponse> getHistory(){
     return null;
    }

    @PostMapping("/undo")
    public ResponseEntity<?> undoAction(@Valid @RequestBody HistoryUndoRequest request){
        return null;
    }
}
