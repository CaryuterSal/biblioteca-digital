package com.edu.utez.bibliotecadigital.model;

import java.io.Serializable;

public interface Entity<ID extends Serializable> {
    ID getId();
}
