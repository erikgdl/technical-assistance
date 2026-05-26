package com.example.assistencia_tecnica.database.repository;

import com.example.assistencia_tecnica.database.model.PecaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPecaRepository extends JpaRepository<PecaEntity, Long> {
}
