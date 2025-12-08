package com.edu.utez.bibliotecadigital.model;

import com.edu.utez.bibliotecadigital.infrastructure.datastructures.SinglyLinkedList;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class User extends Entity<UUID> implements UserDetails, Serializable {
    private final UUID id;
    private String username;
    private String password;
    private boolean enabled = true;
    private SinglyLinkedList<? extends GrantedAuthority> authorities;

    public User(UUID id, String username, String password, SinglyLinkedList<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public @NonNull Collection<? extends GrantedAuthority> getAuthorities() {
        if(authorities == null) return List.of();

        List<GrantedAuthority> authoritiesList = new ArrayList<>();
        for(int i = 0; i < authorities.size(); i++){
            authoritiesList.add(authorities.get(i));
        }
        return authoritiesList;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }
}
