package com.edu.utez.bibliotecadigital.infrastructure.datastructures;

public interface Stack<T> extends Cloneable{
    void push(T element);
    T pop();
    T peek();
    boolean isEmpty();
    int size();
}
