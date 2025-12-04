package com.edu.utez.bibliotecadigital.infrastructure.datastructures;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ArrayQueue<T> implements Queue<T> {

    private T[] items;
    private int front, rear, size;

    @Value("pending-loans.queue.capacity")
    private int capacity;

    public ArrayQueue() {
        items = (T[]) new Object[capacity];
        front = 0;
        rear = 0;
        size = 0;
    }

    @Override
    public void enqueue(T element) {
        if (size == items.length) resize();
        items[rear] = element;
        rear = (rear + 1) % items.length;
        size++;
    }

    @Override
    public T dequeue() {
        if (isEmpty()) return null;
        T value = items[front];
        items[front] = null;
        front = (front + 1) % items.length;
        size--;
        return value;
    }

    @Override
    public T peek() {
        return isEmpty() ? null : items[front];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    private void resize() {
        T[] newArr = (T[]) new Object[items.length * 2];
        for (int i = 0; i < size; i++)
            newArr[i] = items[(front + i) % items.length];
        items = newArr;
        front = 0;
        rear = size;
    }
}
