package com.edu.utez.bibliotecadigital.controller;

import com.edu.utez.bibliotecadigital.controller.dto.UserRegisterRequest;
import com.edu.utez.bibliotecadigital.infrastructure.datastructures.SinglyLinkedList;
import com.edu.utez.bibliotecadigital.model.User;
import com.edu.utez.bibliotecadigital.repository.UsersRepository;
import com.edu.utez.bibliotecadigital.service.AuthService;
import com.edu.utez.bibliotecadigital.service.UserService;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UsersControllerTest extends IntegrationTest{

    @Autowired
    MockMvc mvc;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private ObjectProvider<@NonNull SinglyLinkedList<GrantedAuthority>> authListProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp(){
        usersRepository.clear();
        SinglyLinkedList<GrantedAuthority> authorities = authListProvider.getObject();
        authorities.addLast(new SimpleGrantedAuthority("ROLE_ADMIN"));
        usersRepository.save(new User(UUID.randomUUID(), "admin", passwordEncoder.encode("password"), authorities)); // Register admin
        usersRepository.save(new User(UUID.randomUUID(), "carlos", passwordEncoder.encode("password"), null));
    }

    @AfterEach
    public void tearDown(){
        usersRepository.clear();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllUsers_ShouldReturn200() throws Exception {
        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    @WithMockUser(roles = "ADMIN",
    username = "admin")
    void getUser_ShouldReturn200() throws Exception {
        mvc.perform(get("/users/{id}", usersRepository.findByUsername("carlos").get().getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("carlos"));
    }
}
