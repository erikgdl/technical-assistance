package com.example.assistencia_tecnica.database.repository;

import com.example.assistencia_tecnica.database.model.OrdemServicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IOrdemServicoRepository extends JpaRepository<OrdemServicoEntity, UUID> {
}
