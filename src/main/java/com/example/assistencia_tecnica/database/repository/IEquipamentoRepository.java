package com.example.assistencia_tecnica.database.repository;

import com.example.assistencia_tecnica.database.model.ClienteEntity;
import com.example.assistencia_tecnica.database.model.EquipamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IEquipamentoRepository extends JpaRepository<EquipamentoEntity, UUID> {
}
