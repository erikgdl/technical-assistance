package com.example.assistencia_tecnica.database.repository;

import com.example.assistencia_tecnica.database.model.ClienteEntity;
import com.example.assistencia_tecnica.database.model.TecnicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ITecnicoRepository extends JpaRepository<TecnicoEntity, Long> {
}
