package com.edu.utez.bibliotecadigital.controller.dto;

import com.edu.utez.bibliotecadigital.model.Book;
import com.edu.utez.bibliotecadigital.model.TypeAction;

public record HistoryItemResponse(Book book, TypeAction action){

}
