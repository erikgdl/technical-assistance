package com.example.assistencia_tecnica.database.repository;

import com.example.assistencia_tecnica.database.model.ServicoRealizadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface IServicoRealizadoRepository extends JpaRepository<ServicoRealizadoEntity, Long> {

    @Query("SELECT s FROM ServicoRealizadoEntity s WHERE s.ordemServicoId.id = :osId")
    List<ServicoRealizadoEntity> findByOrdemServicoId(@Param("osId") UUID osId);

    void deleteByOrdemServicoId_Id(UUID ordemServicoId);

    void deleteByServicoId_Id(Long servicoId);
}
