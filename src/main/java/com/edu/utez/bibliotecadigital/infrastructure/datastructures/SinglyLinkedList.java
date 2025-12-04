package com.edu.utez.bibliotecadigital.infrastructure.datastructures;

public interface SinglyLinkedList<T> {


    class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
        }
    }

    void addFirst(T value);
    void addLast(T value);
    T get(int index);
    boolean remove(T value);
    T removeFirst();
    boolean isEmpty();
    int size();
}
