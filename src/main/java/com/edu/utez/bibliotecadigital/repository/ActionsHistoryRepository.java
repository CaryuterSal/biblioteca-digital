package com.edu.utez.bibliotecadigital.repository;

import com.edu.utez.bibliotecadigital.model.HistoryAction;
import com.edu.utez.bibliotecadigital.model.TypeAction;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface ActionsHistoryRepository extends CrudRepository<HistoryAction, UUID> {
}
