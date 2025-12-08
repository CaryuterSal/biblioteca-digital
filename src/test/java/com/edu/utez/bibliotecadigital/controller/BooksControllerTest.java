package com.edu.utez.bibliotecadigital.controller;

import com.edu.utez.bibliotecadigital.model.Book;
import com.edu.utez.bibliotecadigital.repository.BooksRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BooksControllerTest extends IntegrationTest{
    @Autowired
    MockMvc mvc;

    @Autowired
    BooksRepository booksRepository;

    @Test
    @WithMockUser(roles = "ADMIN")
    void addBook_ShouldReturn201() throws Exception {
        String body = """
            {
              "title": "Test Book",
              "author": "test author",
              "availableCopies": 5
            }
            """;

        mvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Book"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void createBookAsUser_ShouldReturn403() throws Exception {
        String body = """
            {
              "title": "Test Book",
              "author": "test author",
              "availableCopies": 5
            }
            """;

        mvc.perform(put("/books/{id}", booksRepository.findAll().get(0).getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden());
    }

    @BeforeEach
    void setUp()throws Exception{

        booksRepository.clear();
        booksRepository.save(new Book(UUID.randomUUID(), 10, "Harry", "Rowling"));
    }

    @AfterEach
    void tearDown() {
        booksRepository.clear();
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllBooks_ShouldReturn200() throws Exception {
        booksRepository.save(new Book(UUID.randomUUID(), 20, "requested", "book author"));
        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[1].title").value("requested"))
                .andExpect(jsonPath("$.[1].author").value("book author"))
                .andExpect(jsonPath("$.[1].availableCopies").value(20))
                .andExpect(jsonPath("$.[0].title").value("Harry"))
                .andExpect(jsonPath("$.[0].author").value("Rowling"))
                .andExpect(jsonPath("$.[0].availableCopies").value(10));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getBooksById_ShouldReturn200() throws Exception {
        mvc.perform(get("/books/{id}", booksRepository.findAll().get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").value("Harry"))
                .andExpect(jsonPath("author").value("Rowling"))
                .andExpect(jsonPath("availableCopies").value(10));
    }

    @Test
    @WithMockUser(roles = "USER")
    void unexistentBook_ShouldReturn404() throws Exception {
        mvc.perform(get("/books/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    void deletedBook_ShouldReturn404() throws Exception {
        Book deletedBook = booksRepository.findAll().get(0);
        booksRepository.delete(deletedBook);
        mvc.perform(get("/books/{id}", deletedBook.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateBook_ShouldReturn200() throws Exception {
        String body = """
                    {
                        "title": "Updated title",
                        "author": "test author",
                        "availableCopies": 10
                    }
                """;
        mvc.perform(put("/books/{id}", booksRepository.findAll().get(0).getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").value("Updated title"))
                .andExpect(jsonPath("author").value("test author"))
                .andExpect(jsonPath("availableCopies").value(10));
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateBookAsUser_ShouldReturn403() throws Exception {
        mvc.perform(put("/books/{id}", booksRepository.findAll().get(0).getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateNotDeletedBookStatus_ShouldReturn200() throws Exception {
        mvc.perform(patch("/books/{id}", booksRepository.findAll().get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("deleted").value(true));
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateBookStatusAsUser_ShouldReturn403() throws Exception {
        mvc.perform(patch("/books/{id}", booksRepository.findAll().get(0).getId()))
                .andExpect(status().isForbidden());
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateDeletedBookStatus_ShouldReturn200() throws Exception {
        Book deletedBook = booksRepository.findAll().get(0);
        deletedBook.setDeleted(true);
        booksRepository.save(deletedBook);
        mvc.perform(patch("/books/{id}", deletedBook.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("deleted").value(false));
    }

    @Test
    @WithMockUser(roles = "USER")
    void searchBook_ShouldReturn200() throws Exception {
        mvc.perform(get("/books/search")
                        .queryParam("title", "Harry"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("Harry"))
                .andExpect(jsonPath("$[0].author").value("Rowling"))
                .andExpect(jsonPath("$[0].availableCopies").value(10));
    }
}
