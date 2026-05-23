package com.example.assistencia_tecnica.database.repository;

import com.example.assistencia_tecnica.database.model.ServicoRealizadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IServicoRealizadoRepository extends JpaRepository<ServicoRealizadoEntity, Long> {
}
