package com.example.assistencia_tecnica.database.repository;

import com.example.assistencia_tecnica.database.model.OrdemServicoEntity;
import com.example.assistencia_tecnica.enums.StatusServicoEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IOrdemServicoRepository extends JpaRepository<OrdemServicoEntity, UUID> {

    Page<OrdemServicoEntity> findByStatus(StatusServicoEnum status, Pageable pageable);

    List<OrdemServicoEntity> findByClienteId(UUID clienteId);

    List<OrdemServicoEntity> findByTecnicoId(Long tecnicoId);

}
