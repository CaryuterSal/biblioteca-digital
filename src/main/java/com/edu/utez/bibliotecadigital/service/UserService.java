package com.edu.utez.bibliotecadigital.service;

import com.edu.utez.bibliotecadigital.controller.dto.UserResponse;
import com.edu.utez.bibliotecadigital.infrastructure.datastructures.SinglyLinkedList;
import com.edu.utez.bibliotecadigital.infrastructure.exceptions.NotFoundException;
import com.edu.utez.bibliotecadigital.model.User;
import com.edu.utez.bibliotecadigital.repository.UsersRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UsersRepository usersRepository;
    private final User staticUserAdmin;

    @PostConstruct
    public void init() {
        usersRepository.save(staticUserAdmin);
    }

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        return usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public List<UserResponse> findUsers(){
        List<UserResponse> response = new ArrayList<>();
        SinglyLinkedList<User> users = usersRepository.findAll();

        for(int i = 0; i < users.size(); i++){
            User user = users.get(i);
            response.add(new UserResponse(
                    user.getId(),
                    user.getUsername()
            ));
        }

        return response;
    }

    public UserResponse findUser(UUID id){
        boolean isAdmin =  SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!(isAdmin || currentUser.getId().equals(id))) throw new AuthorizationDeniedException("Cannot query loans for external users");

        return usersRepository.findById(id)
                .map(user -> new UserResponse(user.getId(), user.getUsername()))
                .orElseThrow(() -> new NotFoundException(User.class, id));
    }
}
