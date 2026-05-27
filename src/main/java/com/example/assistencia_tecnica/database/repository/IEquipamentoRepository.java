package com.example.assistencia_tecnica.database.repository;

import com.example.assistencia_tecnica.database.model.EquipamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IEquipamentoRepository extends JpaRepository<EquipamentoEntity, UUID> {

    List<EquipamentoEntity> findByClienteId(UUID id);

    Optional<EquipamentoEntity> findByNumeroSerie(String numeroSerie);

}
