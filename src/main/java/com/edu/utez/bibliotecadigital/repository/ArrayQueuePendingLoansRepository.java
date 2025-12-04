package com.edu.utez.bibliotecadigital.repository;

import com.edu.utez.bibliotecadigital.infrastructure.datastructures.Queue;
import com.edu.utez.bibliotecadigital.infrastructure.datastructures.SinglyLinkedList;
import com.edu.utez.bibliotecadigital.infrastructure.datastructures.Stack;
import com.edu.utez.bibliotecadigital.model.LoanStatus;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ArrayQueuePendingLoansRepository implements PendingLoansRepository {

    private final ObjectProvider<@NonNull Queue<LoanStatus>> queueProvider;
    private final ObjectProvider<@NonNull SinglyLinkedList<LoanStatus>> listProvider;
    private final Queue<LoanStatus> pendingLoans;


    @Override
    public Optional<LoanStatus> poll() {
        return Optional.ofNullable(pendingLoans.dequeue());
    }

    @Override
    public Optional<LoanStatus> pollForUser(UUID userId) {
        Queue<LoanStatus> tempDeque = queueProvider.getObject();
        LoanStatus result = null;

        while(!pendingLoans.isEmpty()){
            LoanStatus top =  pendingLoans.dequeue();
            if(top.getUser().getId().equals(userId)){
                result = top;
                break;
            }
            tempDeque.enqueue(result);
        }

        while(!tempDeque.isEmpty()){
            pendingLoans.enqueue(tempDeque.dequeue());
        }

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<LoanStatus> peekForUser(UUID userId) {
        Queue<LoanStatus> tempDeque = queueProvider.getObject();
        LoanStatus result = null;

        while(!pendingLoans.isEmpty()){
            LoanStatus top =  pendingLoans.dequeue();
            tempDeque.enqueue(top);
            if(top.getUser().getId().equals(userId)){
                result = top;
                break;
            }
        }

        while(!tempDeque.isEmpty()){
            pendingLoans.enqueue(tempDeque.dequeue());
        }

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<LoanStatus> peek() {
        return Optional.ofNullable(pendingLoans.peek());
    }

    @Override
    public LoanStatus save(LoanStatus entity) {
        pendingLoans.enqueue(entity);
        return entity;
    }

    @Override
    public Optional<LoanStatus> findById(UUID uuid) {
        Queue<LoanStatus> tempDeque = queueProvider.getObject();
        LoanStatus result = null;

        while(!pendingLoans.isEmpty()){
            LoanStatus top =  pendingLoans.dequeue();
            tempDeque.enqueue(top);
            if(top.getId().equals(uuid)){
                result = top;
                break;
            }
        }

        while(!tempDeque.isEmpty()){
            pendingLoans.enqueue(tempDeque.dequeue());
        }

        return Optional.ofNullable(result);
    }

    @Override
    public SinglyLinkedList<LoanStatus> findAll() {
        SinglyLinkedList<LoanStatus> list = listProvider.getObject();
        Queue<LoanStatus> temp = queueProvider.getObject();

        while (!pendingLoans.isEmpty()) {
            LoanStatus top = pendingLoans.dequeue();
            list.addLast(top);
            temp.enqueue(top);
        }

        while (!temp.isEmpty()) {
            pendingLoans.enqueue(temp.dequeue());
        }

        return list;

    }

    @Override
    public void delete(LoanStatus entity) {
        deleteById(entity.getId());
    }

    @Override
    public void deleteById(UUID uuid) {
        Queue<LoanStatus> temp = queueProvider.getObject();

        while (!pendingLoans.isEmpty()) {
            LoanStatus top = pendingLoans.dequeue();
            if (!top.getId().equals(uuid)) {
                temp.enqueue(top);
            }
        }

        while (!temp.isEmpty()) {
            pendingLoans.enqueue(temp.dequeue());
        }
    }
}
