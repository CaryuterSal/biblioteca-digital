package com.edu.utez.bibliotecadigital.controller.dto;

import com.edu.utez.bibliotecadigital.model.Book;

import java.util.List;

public record HistoryResponse(UserResponse subject, List<HistoryItemResponse> items) {
}

record HistoryItemResponse(Book book, HistoryAction action){

}
