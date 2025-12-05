package com.edu.utez.bibliotecadigital.config;

import com.edu.utez.bibliotecadigital.infrastructure.datastructures.SinglyLinkedList;
import com.edu.utez.bibliotecadigital.model.User;
import lombok.NonNull;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.rmi.server.UID;
import java.util.List;
import java.util.UUID;

@Configuration
public class StaticAdminConfig {

    @Bean
    public User staticAdminUser(ObjectProvider<@NonNull SinglyLinkedList<GrantedAuthority>> authListProvider,
    AdminProperties adminProperties, PasswordEncoder passwordEncoder) {
        SinglyLinkedList<GrantedAuthority> authorities = authListProvider.getObject();
        authorities.addLast(new SimpleGrantedAuthority("ROLE_ADMIN"));

        User admin = new User(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                adminProperties.username(),
                adminProperties.password(),
                authorities
                );

        return admin;
    }
}
