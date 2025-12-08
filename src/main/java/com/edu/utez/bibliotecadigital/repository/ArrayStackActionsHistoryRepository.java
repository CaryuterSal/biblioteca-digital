package com.edu.utez.bibliotecadigital.repository;

import com.edu.utez.bibliotecadigital.infrastructure.datastructures.ArrayStack;
import com.edu.utez.bibliotecadigital.infrastructure.datastructures.SinglyLinkedList;
import com.edu.utez.bibliotecadigital.infrastructure.datastructures.Stack;
import com.edu.utez.bibliotecadigital.model.LoanStatus;
import com.edu.utez.bibliotecadigital.model.TypeAction;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ArrayStackActionsHistoryRepository implements ActionsHistoryRepository {

    private final ObjectProvider<@NonNull Stack<LoanStatus>> stackProvider;
    private final ObjectProvider<@NonNull SinglyLinkedList<LoanStatus>> listProvider;

    private final ArrayStack<LoanStatus> history;

    @Override
    public Optional<LoanStatus> pop() {
        return Optional.ofNullable(history.pop());
    }

    @Override
    public Optional<LoanStatus> peek() {
        return Optional.ofNullable(history.peek());
    }

    @Override
    public Optional<LoanStatus> peekForUser(UUID userId) {
        Stack<LoanStatus> tempPopoff = stackProvider.getObject();
        LoanStatus result = null;

        while(!history.isEmpty()){
            LoanStatus top = history.pop();
            tempPopoff.push(top);
            if(top.getUser().getId().equals(userId)){
                result = top;
                break;
            }
        }

        while(!tempPopoff.isEmpty()){
            history.push(tempPopoff.pop());
        }

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<LoanStatus> popForUser(UUID userId) {
        Stack<LoanStatus> tempPopoff = stackProvider.getObject();
        LoanStatus result = null;

        while(!history.isEmpty()){
            LoanStatus top = history.pop();
            if(top.getUser().getId().equals(userId)){
                result = top;
                break;
            }
            tempPopoff.push(top);
        }

        while(!tempPopoff.isEmpty()){
            history.push(tempPopoff.pop());
        }

        return Optional.ofNullable(result);
    }

    @Override
    public Stack<LoanStatus> findForUser(UUID userId) {
        Stack<LoanStatus> tempPopoff = stackProvider.getObject();
        Stack<LoanStatus> userHistory = stackProvider.getObject();

        while(!history.isEmpty()){
            LoanStatus top = history.pop();
            tempPopoff.push(top);
        }

        while(!tempPopoff.isEmpty()){
            LoanStatus top = tempPopoff.pop();
            history.push(top);
            if(top.getUser().getId().equals(userId)){
                userHistory.push(top);
            }
        }

        return userHistory;
    }

    @Override
    public Stack<LoanStatus> findActiveLoansForUser(UUID userId) {
        Stack<LoanStatus> userHistory = findForUser(userId);
        SinglyLinkedList<LoanStatus> activeUserLoans = listProvider.getObject();

        LoanStatus[] buffer = new LoanStatus[userHistory.size()];

        int idx = 0;
        while (!userHistory.isEmpty()) {
            buffer[idx++] = userHistory.pop();
        }

        for (int i = 0; i < idx; i++) {
            LoanStatus action = buffer[i];

            if (action.getTypeAction() == TypeAction.CREATE_LOAN) {
                activeUserLoans.addLast(action);
            }

            if (action.getTypeAction() == TypeAction.RETURN_LOAN) {
                activeUserLoans.remove(action);
            }
        }

        Stack<LoanStatus> activeLoansStack = stackProvider.getObject();
        for(int i = 0; i < activeUserLoans.size(); i++){
            activeLoansStack.push(activeUserLoans.get(i));
        }

        return activeLoansStack;
    }

    @Override
    public Optional<LoanStatus> popForUserByAction(UUID userId, TypeAction typeAction) {
        Stack<LoanStatus> tempPopoff = stackProvider.getObject();
        LoanStatus result = null;

        while(!history.isEmpty()){
            LoanStatus top = history.pop();
            if(top.getUser().getId().equals(userId) && top.getTypeAction().equals(typeAction)){
                result = top;
                break;
            }
            tempPopoff.push(top);
        }

        while(!tempPopoff.isEmpty()){
            history.push(tempPopoff.pop());
        }

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<LoanStatus> popByAction(TypeAction typeAction) {
        Stack<LoanStatus> tempPopoff = stackProvider.getObject();
        LoanStatus result = null;

        while(!history.isEmpty()){
            LoanStatus top = history.pop();
            if(top.getTypeAction().equals(typeAction)){
                result = top;
                break;
            }
            tempPopoff.push(top);
        }

        while(!tempPopoff.isEmpty()){
            history.push(tempPopoff.pop());
        }

        return Optional.ofNullable(result);
    }

    @Override
    public Stack<LoanStatus> findActiveLoans() {
        SinglyLinkedList<LoanStatus> activeUserLoans = listProvider.getObject();

        LoanStatus[] buffer = new LoanStatus[history.size()];

        int idx = 0;
        while (!history.isEmpty()) {
            buffer[idx++] = history.pop();
        }

        for(int i = history.size() - 1; i >= 0; i--){
            history.push(buffer[i]);
        }

        for (int i = 0; i < idx; i++) {
            LoanStatus action = buffer[i];

            if (action.getTypeAction() == TypeAction.CREATE_LOAN) {
                activeUserLoans.addLast(action);
            }

            if (action.getTypeAction() == TypeAction.RETURN_LOAN) {
                activeUserLoans.remove(action);
            }
        }

        Stack<LoanStatus> activeLoansStack = stackProvider.getObject();
        for(int i = 0; i < activeUserLoans.size(); i++){
            activeLoansStack.push(activeUserLoans.get(i));
        }

        return activeLoansStack;
    }


    @Override
    public LoanStatus save(LoanStatus entity) {
        history.push(entity);
        return entity;
    }

    @Override
    public Optional<LoanStatus> findById(UUID uuid) {
        Stack<LoanStatus> tempPopoff = stackProvider.getObject();
        LoanStatus result = null;
        while(!history.isEmpty()){
            LoanStatus top = history.pop();
            tempPopoff.push(top);
            if(top.getId().equals(uuid)){
                result = top;
                break;
            }
        }

        while(!tempPopoff.isEmpty()){
            history.push(tempPopoff.pop());
        }

        return Optional.ofNullable(result);
    }

    @Override
    public SinglyLinkedList<LoanStatus> findAll() {
        SinglyLinkedList<LoanStatus> all =  listProvider.getObject();

        Stack<LoanStatus> tempPopoff = stackProvider.getObject();
        LoanStatus result = null;
        while(!history.isEmpty()){
            LoanStatus top = history.pop();
            tempPopoff.push(top);
            all.addLast(top);
        }

        while(!tempPopoff.isEmpty()){
            history.push(tempPopoff.pop());
        }

        return all;
    }

    @Override
    public void delete(LoanStatus entity) {
        deleteById(entity.getId());
    }

    @Override
    public void deleteById(UUID uuid) {
        Stack<LoanStatus> tempPopoff = stackProvider.getObject();

        while(!history.isEmpty()){
            LoanStatus top = history.pop();
            if(top.getId().equals(uuid)){
                break;
            }
            tempPopoff.push(top);
        }

        while(!tempPopoff.isEmpty()){
            history.push(tempPopoff.pop());
        }
    }

    @Override
    public void clear() {
        while(!history.isEmpty()){
            history.pop();
        }
    }
}
