package com.example.assistencia_tecnica.service;

import com.example.assistencia_tecnica.database.model.*;
import com.example.assistencia_tecnica.database.repository.*;
import com.example.assistencia_tecnica.dto.*;
import com.example.assistencia_tecnica.enums.StatusServicoEnum;
import com.example.assistencia_tecnica.exception.BadRequestException;
import com.example.assistencia_tecnica.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrdemServicoService {

    private final IClienteRepository clienteRepository;
    private final IEquipamentoRepository equipamentoRepository;
    private final IOrdemServicoRepository ordemServicoRepository;
    private final IPecaRepository pecaRepository;
    private final IServicoRealizadoRepository servicoRealizadoRepository;
    private final ITecnicoRepository tecnicoRepository;
    private final IServicoRepository servicoRepository;
    private final IPecaUtilizadaRepository pecaUtilizadaRepository;

    public OrdemServicoEntity abrirOS(OrdemServicoDto dto) {
        ClienteEntity cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));

        EquipamentoEntity equipamento = equipamentoRepository.findById(dto.getEquipamentoId())
                .orElseThrow(() -> new RuntimeException("Equipamento não encontrado!"));


        if (!equipamento.getCliente().getId().equals(cliente.getId())) {
            throw new RuntimeException("Este equipamento não pertence ao cliente informado!");
        }

        OrdemServicoEntity novaOs = OrdemServicoEntity.builder()
                .cliente(cliente)
                .equipamento(equipamento)
                .defeitoRelatado(dto.getDefeitoRelatado())
                .status(StatusServicoEnum.ABERTA)
                .valorTotal(BigDecimal.ZERO)
                .dataAbertura(LocalDateTime.now())
                //.numeroOs(UUID.randomUUID())
                .build();

        return ordemServicoRepository.save(novaOs);
    }

    public OrdemServicoEntity iniciarAnalise(UUID ordemServicoId, Long tecnicoId) {

        OrdemServicoEntity os = ordemServicoRepository.findById(ordemServicoId)
                .orElseThrow(() -> new RuntimeException("Ordem de Serviço não encontrada!"));


        TecnicoEntity tecnico = tecnicoRepository.findById(tecnicoId)
                .orElseThrow(() -> new RuntimeException("Técnico não encontrado com o ID informado!"));

        if (os.getStatus() != StatusServicoEnum.ABERTA) {
            throw new RuntimeException("só pode ir para análise se estiver com o status ABERTA.");
        }

        os.setStatus(StatusServicoEnum.EM_ANALISE);
        os.setTecnico(tecnico);

        return ordemServicoRepository.save(os);
    }

    public OrdemServicoEntity registrarLaudo(UUID ordemServicoId, RegistrarLaudoDto dto) {

        OrdemServicoEntity os = ordemServicoRepository.findById(ordemServicoId)
                .orElseThrow(() -> new RuntimeException("Ordem de Serviço não encontrada!"));

        if (os.getStatus() != StatusServicoEnum.EM_ANALISE) {
            throw new RuntimeException("Só é possível registrar um laudo se estiver EM_ANALISE.");
        }

        os.setLaudoTecnico(dto.laudoTecnico());
        return ordemServicoRepository.save(os);
    }

    public OrdemServicoEntity adicionarPeca(UUID ordemServicoId, AdicionarPecaDto dto) throws BadRequestException, NotFoundException {

        OrdemServicoEntity os = ordemServicoRepository.findById(ordemServicoId)
                .orElseThrow(() -> new RuntimeException("Ordem de Serviço não encontrada!"));

        if (os.getStatus() != StatusServicoEnum.EM_ANALISE) {
            throw new BadRequestException("Não é possível adicionar peças. A OS não está em análise.");
        }

        PecaEntity peca = pecaRepository.findById(dto.pecaId())
                .orElseThrow(() -> new NotFoundException("Peca informado."));

        if (peca.getQuantidadeEstoque() < dto.quantidadeEstoque()) {
            throw new BadRequestException("Estoque insuficiente! Temos apenas " + peca.getQuantidadeEstoque() + " unidades disponíveis.");
        }

        PecaUtilizadaEntity pecaUtilizada = PecaUtilizadaEntity.builder()
                .ordemServicoId(os)
                .PecaId(peca)
                .quantidade(dto.quantidadeEstoque())
                .precoUnitarioMomento(peca.getPrecoUnitario())
                .build();

        pecaUtilizadaRepository.save(pecaUtilizada);

        BigDecimal totalDestaPeca = peca.getPrecoUnitario().multiply(new BigDecimal(dto.quantidadeEstoque()));

        os.setValorTotal(os.getValorTotal().add(totalDestaPeca));

        return ordemServicoRepository.save(os);
    }

    public OrdemServicoEntity enviarParaAprovacao(UUID ordemServiceId) throws NotFoundException, BadRequestException {
        OrdemServicoEntity os = ordemServicoRepository.findById(ordemServiceId)
                .orElseThrow(() -> new NotFoundException("Ordem de Serviço não encontrada!"));

        if (os.getStatus() != StatusServicoEnum.EM_ANALISE) {
            throw new BadRequestException("Não é possível adicionar serviços. A OS não está em análise.");
        }

        if (os.getLaudoTecnico() == null || os.getLaudoTecnico().trim().isEmpty()) {
            throw new BadRequestException("Impossível enviar para aprovação sem o laudo técnico preenchido.");
        }

        if (os.getValorTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("O orçamento não tem itens. Adicione peças ou serviços antes de aprovar.");
        }

        os.setStatus(StatusServicoEnum.AGUARDANDO_APROVACAO_CLIENTE);
        return ordemServicoRepository.save(os);
    }

    @Transactional(rollbackFor = Exception.class)
    public OrdemServicoEntity aprovarOS(UUID ordemServicoId) throws Exception {
        OrdemServicoEntity os = ordemServicoRepository.findById(ordemServicoId)
                .orElseThrow(() -> new NotFoundException("Ordem de Serviço não encontrada!"));

        if (os.getStatus() != StatusServicoEnum.AGUARDANDO_APROVACAO_CLIENTE) {
            throw new BadRequestException("A OS não está aguardando aprovação do cliente.");
        }

        List<PecaUtilizadaEntity> pecasDaOs = pecaUtilizadaRepository.findByOrdemServicoId(ordemServicoId);

        for (PecaUtilizadaEntity item : pecasDaOs) {
            PecaEntity pecaNoBanco = item.getPecaId();

            if (pecaNoBanco.getQuantidadeEstoque() < item.getQuantidade()) {
                throw new BadRequestException("Estoque insuficiente para a peça: " + pecaNoBanco.getNome() + ". Aprovação cancelada.");
            }

            int novoEstoque = pecaNoBanco.getQuantidadeEstoque() - item.getQuantidade();
            pecaNoBanco.setQuantidadeEstoque(novoEstoque);

            pecaRepository.save(pecaNoBanco);
        }

        os.setStatus(StatusServicoEnum.APROVADA);
        return ordemServicoRepository.save(os);
    }

    public OrdemServicoEntity iniciarManutencao(UUID ordemServicoId) throws NotFoundException, BadRequestException {
        OrdemServicoEntity os = ordemServicoRepository.findById(ordemServicoId)
                .orElseThrow(() -> new NotFoundException("Ordem de Serviço não encontrada!"));

        if (os.getStatus() != StatusServicoEnum.APROVADA) {
            throw new BadRequestException("A Ordem Serviço precisa estar APROVADA para iniciar a manutenção.");
        }

        os.setStatus(StatusServicoEnum.EM_MANUTENCAO);
        return ordemServicoRepository.save(os);
    }

    public OrdemServicoEntity reabrirManutencao(UUID ordemServicoId) throws Exception {
        OrdemServicoEntity os = ordemServicoRepository.findById(ordemServicoId)
                .orElseThrow(() -> new NotFoundException("Ordem de Serviço não encontrada!"));

        if (os.getStatus() == StatusServicoEnum.ENTREGUE || os.getStatus() == StatusServicoEnum.CONCLUIDA) {
            throw new BadRequestException("Não é possível reabrir orçamento de uma Ordem de Serviço já concluída ou entregue.");
        }
        os.setStatus(StatusServicoEnum.EM_ANALISE);
        return ordemServicoRepository.save(os);
    }

    public OrdemServicoEntity concluirManutencao(UUID ordemServicoId) throws Exception {
        OrdemServicoEntity os = ordemServicoRepository.findById(ordemServicoId)
                .orElseThrow(() -> new NotFoundException("Ordem de Serviço não encontrada!"));

        if (os.getStatus() != StatusServicoEnum.EM_MANUTENCAO) {
            throw new BadRequestException("A OS precisa estar EM MANUTENÇÃO para ser concluída.");
        }

        os.setStatus(StatusServicoEnum.CONCLUIDA);
        os.setDataConclusao(LocalDateTime.now());

        return ordemServicoRepository.save(os);
    }

    public OrdemServicoEntity entregarEquipamento(UUID ordemServicoId) throws Exception {
        OrdemServicoEntity os = ordemServicoRepository.findById(ordemServicoId)
                .orElseThrow(() -> new NotFoundException("Ordem de Serviço não encontrada!"));

        if (os.getStatus() != StatusServicoEnum.CONCLUIDA) {
            throw new BadRequestException("O equipamento só pode ser entregue se a manutenção estiver CONCLUÍDA.");
        }

        os.setStatus(StatusServicoEnum.ENTREGUE);

        return ordemServicoRepository.save(os);
    }

    public OrdemServicoEntity adicionarServico(UUID ordemServicoId, AdicionarServicoDto dto) throws BadRequestException, NotFoundException {

        OrdemServicoEntity os = ordemServicoRepository.findById(ordemServicoId)
                .orElseThrow(() -> new NotFoundException("Ordem de Serviço não encontrada!"));

        if (os.getStatus() != StatusServicoEnum.EM_ANALISE) {
            throw new BadRequestException("Não é possível adicionar serviços. A OS não está em análise.");
        }

        ServicoEntity servico = servicoRepository.findById(dto.id())
                .orElseThrow(() -> new NotFoundException("Serviço não encontrado no catálogo!"));

        ServicoRealizadoEntity servicoRealizado = ServicoRealizadoEntity.builder()
                .ordemServicoId(os)
                .servicoId(servico)
                .precoCobrado(servico.getPrecoBase())
                .build();

        servicoRealizadoRepository.save(servicoRealizado);

        os.setValorTotal(os.getValorTotal().add(servico.getPrecoBase()));

        return ordemServicoRepository.save(os);
    }
}
