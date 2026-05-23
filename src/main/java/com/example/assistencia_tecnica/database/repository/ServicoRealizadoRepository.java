package com.example.assistencia_tecnica.database.repository;

import com.example.assistencia_tecnica.database.model.ServicoRealizadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ServicoRealizadoRepository extends JpaRepository<ServicoRealizadoEntity, Long> {
}
