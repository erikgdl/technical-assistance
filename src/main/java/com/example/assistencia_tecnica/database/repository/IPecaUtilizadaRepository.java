package com.example.assistencia_tecnica.database.repository;

import com.example.assistencia_tecnica.database.model.OrdemServicoEntity;
import com.example.assistencia_tecnica.database.model.PecaUtilizadaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface IPecaUtilizadaRepository extends JpaRepository<PecaUtilizadaEntity, Long>{

    @Query("SELECT p FROM PecaUtilizadaEntity p WHERE p.ordemServicoId.id = :osId")
    List<PecaUtilizadaEntity> findByOrdemServicoId(@Param("osId") UUID ordemServicoId);

    void deleteByOrdemServicoId_Id(UUID ordemServicoId);

    void deleteByPecaId_Id(Long pecaId);

    @Modifying
    @Query("DELETE FROM PecaUtilizadaEntity p WHERE p.PecaId.id = :pecaId")
    void deleteByPecaId(@Param("pecaId") Long pecaId);
}
