package com.example.assistencia_tecnica.database.repository;

import com.example.assistencia_tecnica.database.model.TecnicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ITecnicoRepository extends JpaRepository<TecnicoEntity, Long> {

    boolean existsByMatricula(String matricula);

    Optional<TecnicoEntity> findByMatricula(String matricula);
}
