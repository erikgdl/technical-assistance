package com.example.assistencia_tecnica.service;

import com.example.assistencia_tecnica.database.model.ClienteEntity;
import com.example.assistencia_tecnica.database.model.TecnicoEntity;
import com.example.assistencia_tecnica.database.repository.IClienteRepository;
import com.example.assistencia_tecnica.database.repository.ITecnicoRepository;
import com.example.assistencia_tecnica.dto.ClienteDto;
import com.example.assistencia_tecnica.dto.TecnicoDto;
import com.example.assistencia_tecnica.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class TecnicoService {

    private final ITecnicoRepository tecnicoRepository;

    public TecnicoEntity criarTecnico(TecnicoDto dto) {
        String matricula = gerarMatriculaUnica();
        return tecnicoRepository.save(TecnicoEntity.builder()
                        .matricula(matricula)
                        .nome(dto.getNome())
                        .especialidade(dto.getEspecialidade())
                .build());
    }

    private String gerarMatriculaUnica() {
        String matricula;
        do {
            int numeroAleatorio = 10000 + new Random().nextInt(90000);
            matricula = String.valueOf(numeroAleatorio);

        } while (tecnicoRepository.existsByMatricula(matricula));

        return matricula;
    }

}
