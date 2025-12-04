package com.edu.utez.bibliotecadigital.model;

import java.io.Serializable;

public abstract class Entity<ID extends Serializable> {
    public abstract ID getId();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Entity<?> other = (Entity<?>) o;
        return getId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
