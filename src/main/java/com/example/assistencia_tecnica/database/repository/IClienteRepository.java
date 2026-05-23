package com.example.assistencia_tecnica.database.repository;

import com.example.assistencia_tecnica.database.model.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IClienteRepository extends JpaRepository<ClienteEntity, UUID> {

    Optional<ClienteEntity> findByEmail(String email);

    Optional<ClienteEntity> findById(UUID id);

    Optional<ClienteEntity> findByCpf(String cpf);
}
