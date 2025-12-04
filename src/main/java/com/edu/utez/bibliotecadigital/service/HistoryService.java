package com.edu.utez.bibliotecadigital.service;

import com.edu.utez.bibliotecadigital.controller.dto.HistoryItemResponse;
import com.edu.utez.bibliotecadigital.controller.dto.HistoryResponse;
import com.edu.utez.bibliotecadigital.controller.dto.HistoryUndoRequest;
import com.edu.utez.bibliotecadigital.controller.dto.UserResponse;
import com.edu.utez.bibliotecadigital.infrastructure.datastructures.SinglyLinkedList;
import com.edu.utez.bibliotecadigital.infrastructure.datastructures.Stack;
import com.edu.utez.bibliotecadigital.infrastructure.exceptions.NotFoundException;
import com.edu.utez.bibliotecadigital.model.LoanStatus;
import com.edu.utez.bibliotecadigital.model.TypeAction;
import com.edu.utez.bibliotecadigital.model.User;
import com.edu.utez.bibliotecadigital.repository.ActionsHistoryRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final ActionsHistoryRepository repository;

    public List<HistoryResponse> getAllHistory(){
        boolean isAdmin =  SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if(!isAdmin) throw new AuthorizationDeniedException("Cannot query history for external users");

        SinglyLinkedList<LoanStatus> allHistory = repository.findAll();
        List<LoanStatus> loanStatuses = new ArrayList<>();

        for (int i = 0; i < allHistory.size(); i++) {
            loanStatuses.add(allHistory.get(i));
        }

        Map<UUID, List<LoanStatus>> groupedByUser = new HashMap<>();

        for (LoanStatus ls : loanStatuses) {
            UUID userId = ls.getUser().getId();
            groupedByUser.computeIfAbsent(userId, k -> new ArrayList<>()).add(ls);
        }

        List<HistoryResponse> response = new ArrayList<>();

        for (var entry : groupedByUser.entrySet()) {
            UUID userId = entry.getKey();
            List<LoanStatus> userHistory = entry.getValue();

            User user = userHistory.get(0).getUser(); // cualquiera sirve

            response.add(
                    new HistoryResponse(
                            new UserResponse(user.getId(), user.getUsername()),
                            userHistory.stream()
                                    .map(ls -> new HistoryItemResponse(ls.getBook(), ls.getTypeAction()))
                                    .toList()
                    )
            );
        }

        return response;
    }

    public HistoryResponse getHistoryForCurrent(UUID userId){
        boolean isAdmin =  SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!(isAdmin || currentUser.getId().equals(userId))) throw new AuthorizationDeniedException("Cannot query loans for external users");

        List<LoanStatus> result = new ArrayList<>();

        Stack<LoanStatus> historyAsStack = repository.findForUser(currentUser.getId());
        while(!historyAsStack.isEmpty()){
            result.add(historyAsStack.pop());
        }
        return new HistoryResponse(
                new UserResponse(currentUser.getId(), currentUser.getUsername()),
                result.stream().map(
                        ls -> new HistoryItemResponse(ls.getBook(), ls.getTypeAction())
                ).toList()
        );
    }

    public List<HistoryItemResponse> undoAction(HistoryUndoRequest request){
        boolean isAdmin =  SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int steps = request.steps().orElse(1);
        List<LoanStatus> undone = new ArrayList<>();

        if (isAdmin) {
            for (int i = 0; i < steps; i++) {
                Optional<LoanStatus> removed;

                if (request.action().isPresent()) {
                   removed = repository.popByAction(request.action().get());
                } else {
                    removed = repository.pop();
                }

                if (removed.isEmpty()) break;
                undone.add(removed.get());
            }
        } else {
            for (int i = 0; i < steps; i++) {
                Optional<LoanStatus> removed;

                if (request.action().isPresent()) {
                    removed = repository.popForUserByAction(currentUser.getId(), request.action().get());
                } else {
                    removed = repository.pop();
                }

                if (removed.isEmpty()) break;
                undone.add(removed.get());
            }
        }

        return undone.stream()
                .map(hit -> new HistoryItemResponse(
                    hit.getBook(),
                    hit.getTypeAction())
                )
                .toList();
    }
}
