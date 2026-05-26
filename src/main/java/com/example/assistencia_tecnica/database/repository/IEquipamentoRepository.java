package com.example.assistencia_tecnica.database.repository;

import com.example.assistencia_tecnica.database.model.EquipamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface IEquipamentoRepository extends JpaRepository<EquipamentoEntity, UUID> {

    Optional<EquipamentoEntity> findById(Long id);

}
