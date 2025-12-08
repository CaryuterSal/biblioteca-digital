package com.edu.utez.bibliotecadigital.controller;

import com.edu.utez.bibliotecadigital.controller.dto.LoanRequest;
import com.edu.utez.bibliotecadigital.controller.dto.UserRegisterRequest;
import com.edu.utez.bibliotecadigital.infrastructure.datastructures.SinglyLinkedList;
import com.edu.utez.bibliotecadigital.model.Book;
import com.edu.utez.bibliotecadigital.model.User;
import com.edu.utez.bibliotecadigital.repository.ActionsHistoryRepository;
import com.edu.utez.bibliotecadigital.repository.BooksRepository;
import com.edu.utez.bibliotecadigital.repository.PendingLoansRepository;
import com.edu.utez.bibliotecadigital.repository.UsersRepository;
import com.edu.utez.bibliotecadigital.service.AuthService;
import com.edu.utez.bibliotecadigital.service.BooksLoanService;
import com.edu.utez.bibliotecadigital.service.BooksRegistryService;
import com.edu.utez.bibliotecadigital.service.UserService;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HistoryControllerTest extends IntegrationTest{

    @Autowired
    MockMvc mvc;

    @Autowired
    ActionsHistoryRepository  actionsHistoryRepository;

    @Autowired
    BooksLoanService booksLoanService;

    @Autowired
    BooksRepository booksRepository;

    @Autowired
    BooksRegistryService booksRegistryService;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    PendingLoansRepository pendingLoansRepository;

    @Autowired
    AuthService authService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ObjectProvider<@NonNull SinglyLinkedList<GrantedAuthority>> authListProvider;

    @BeforeEach
    void setUp() {

        booksRepository.clear();
        usersRepository.clear();
        actionsHistoryRepository.clear();
        pendingLoansRepository.clear();

        authService.register(new UserRegisterRequest("carlos", "123")); // Register regular user
        SinglyLinkedList<GrantedAuthority> authorities = authListProvider.getObject();
        authorities.addLast(new SimpleGrantedAuthority("ROLE_ADMIN"));
        usersRepository.save(new User(UUID.randomUUID(), "admin", passwordEncoder.encode("password"), authorities)); // Register admin
        booksRepository.save(new Book(UUID.randomUUID(), 4, "book", "John")); // register mock books
        booksRepository.save(new Book(UUID.randomUUID(), 1, "requested book", "John"));
    }

    @AfterEach
    void tearDown() {
        booksRepository.clear();
        usersRepository.clear();
        actionsHistoryRepository.clear();
        pendingLoansRepository.clear();
    }

    @Test
    @WithMockUser(
            roles = "USER",
            username = "carlos",
            password = "123"
    )
    void getHistory_ShouldReturn200() throws Exception {
        booksLoanService.createLoan(new LoanRequest(
                booksRegistryService.searchByTitle("requested book").getFirst().getId(),
                3
                ));
        booksLoanService.createLoan(new LoanRequest(
                booksRegistryService.searchByTitle("requested book").getFirst().getId(),
                1
        ));
        booksLoanService.createLoan(new LoanRequest(
                booksRegistryService.searchByTitle("requested book").getFirst().getId(),
                5
        ));
        mvc.perform(get("/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].items").isArray())
                .andExpect(jsonPath("$[0].items.size()").value(3))
                .andExpect(jsonPath("$[0].items[2].action").value("CREATE_LOAN"))
                .andExpect(jsonPath("$[0].items[1].action").value("ADD_TO_WAITLIST"))
                .andExpect(jsonPath("$[0].items[0].action").value("ADD_TO_WAITLIST"));
    }

    @Test
    @WithMockUser(
            roles = "USER",
            username = "carlos",
            password = "123"
    )
    void undoHistory_ShouldReturn200() throws Exception {
        booksLoanService.createLoan(new LoanRequest(
                booksRegistryService.searchByTitle("requested book").getFirst().getId(),
                3
        ));
        booksLoanService.createLoan(new LoanRequest(
                booksRegistryService.searchByTitle("requested book").getFirst().getId(),
                1
        ));

        String body = """
                {
                    
                }
                """;

        mvc.perform(post("/history/undo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].action").value("ADD_TO_WAITLIST"));
    }
}
