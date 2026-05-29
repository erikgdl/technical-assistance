package com.example.assistencia_tecnica.service;

import com.example.assistencia_tecnica.database.model.TecnicoEntity;
import com.example.assistencia_tecnica.database.repository.ITecnicoRepository;
import com.example.assistencia_tecnica.dto.TecnicoDto;
import com.example.assistencia_tecnica.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<TecnicoEntity> getListarTecnico() {
        return tecnicoRepository.findAll();
    }

    public TecnicoEntity getBuscarPorId(Long id) throws NotFoundException {
        return tecnicoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado com o ID: " + id));
    }

    public TecnicoEntity getBuscarPorMatricula(String matricula) throws NotFoundException {
        return tecnicoRepository.findByMatricula(matricula)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado com o ID: " + matricula));
    }

    public TecnicoEntity atualizarTecnico(Long id, TecnicoDto dto) throws NotFoundException {
        TecnicoEntity tecnicoExistente = tecnicoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Técnico não encontrado"));

        tecnicoExistente.setNome(dto.getNome());
        tecnicoExistente.setEspecialidade(dto.getEspecialidade());
        return tecnicoRepository.save(tecnicoExistente);
    }


}
