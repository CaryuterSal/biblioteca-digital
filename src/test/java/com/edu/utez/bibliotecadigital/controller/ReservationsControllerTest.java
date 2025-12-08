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
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReservationsControllerTest extends IntegrationTest{
    @Autowired
    MockMvc mvc;


    @Autowired
    ActionsHistoryRepository actionsHistoryRepository;

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
        Book toLoanBook = booksRepository.save(new Book(UUID.randomUUID(), 1, "requested book", "John"));


        Authentication previousAuthentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = org.springframework.security.core.userdetails.User.builder()
                .username("carlos")
                .roles("USER")
                .build();
        SecurityContextHolder.getContext().setAuthentication(UsernamePasswordAuthenticationToken.authenticated(user, null, user.getAuthorities()));
        booksLoanService.createLoan(new LoanRequest(toLoanBook.getId(), 1));
        booksLoanService.createLoan(new LoanRequest(toLoanBook.getId(), 3));
        booksLoanService.createLoan(new LoanRequest(toLoanBook.getId(), 5));
        SecurityContextHolder.getContext().setAuthentication(previousAuthentication);

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
            roles = "ADMIN"
    )
    void getPendingReservations_ShouldReturn200() throws Exception {
        mvc.perform(get("/reservations/book/{id}", booksRegistryService.searchByTitle("requested book").getFirst().getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].expectedLoanDurationDays").value(3))
                .andExpect(jsonPath("$[1].expectedLoanDurationDays").value(5));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deletePendingReservations_ShouldReturn204() throws Exception {
        UUID requestedBookId = booksRegistryService.searchByTitle("requested book").getFirst().getId();

        mvc.perform(delete("/reservations")
                        .queryParam("userId", usersRepository.findByUsername("carlos").get().getId().toString())
                        .queryParam("bookId", requestedBookId.toString()))
                .andExpect(status().isNoContent());

        assertThat(booksLoanService.findReservationsForBook(requestedBookId))
                .hasSize(1);
    }
}
