package com.edu.utez.bibliotecadigital.infrastructure.datastructures;

public interface Queue<T> {
    void enqueue(T element);
    T dequeue();
    T peek();
    boolean isEmpty();
    int size();
}
