package com.example.assistencia_tecnica.database.repository;

import com.example.assistencia_tecnica.database.model.ServicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IServicoRepository extends JpaRepository<ServicoEntity, Long> {



}
