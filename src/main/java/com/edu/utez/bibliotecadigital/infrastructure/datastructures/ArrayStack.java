package com.edu.utez.bibliotecadigital.infrastructure.datastructures;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ArrayStack<T> implements Stack<T> {

    private T[] items;
    private int top;
    @Value("${history.stack.capacity}")
    private int CAPACITY;

    public ArrayStack() {
        items = (T[]) new Object[CAPACITY];
        top = 0;
    }

    @Override
    public void push(T element) {
        if (top == items.length) {
            resize();
        }
        items[top++] = element;
    }

    @Override
    public T pop() {
        if (isEmpty()) return null;
        T value = items[--top];
        items[top] = null;
        return value;
    }

    @Override
    public T peek() {
        return isEmpty() ? null : items[top - 1];
    }

    @Override
    public boolean isEmpty() {
        return top == 0;
    }

    @Override
    public int size() {
        return top;
    }

    private void resize() {
        T[] newArray = (T[]) new Object[items.length * 2];
        for (int i = 0; i < items.length; i++) newArray[i] = items[i];
        items = newArray;
    }
}
