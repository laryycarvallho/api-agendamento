package com.examplo.agendamento.service;

import com.examplo.agendamento.dto.AgendamentoRequest;
import com.examplo.agendamento.exception.RecursoNaoEncontradoException;
import com.examplo.agendamento.exception.RegraNegocioException;
import com.examplo.agendamento.model.Agendamento;
import com.examplo.agendamento.model.Cliente;
import com.examplo.agendamento.model.Profissional;
import com.examplo.agendamento.model.StatusAgendamento;
import com.examplo.agendamento.repository.AgendamentoRepository;
import com.examplo.agendamento.repository.ClienteRepository;
import com.examplo.agendamento.repository.ProfissionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final ClienteRepository clienteRepository;
    private final ProfissionalRepository profissionalRepository;

    @Transactional
    public Agendamento criar(AgendamentoRequest request) {
        if (!request.fim().isAfter(request.inicio())) {
            throw new RegraNegocioException("O fim deve ser depois do início");
        }

        Cliente cliente = clienteRepository.findById(request.clienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado"));

        Profissional profissional = profissionalRepository.findById(request.profissionalId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Profissional não encontrado"));

        if (agendamentoRepository.existeConflito(profissional.getId(), request.inicio(), request.fim())) {
            throw new RegraNegocioException("Horário indisponível para este profissional");
        }

        Agendamento agendamento = new Agendamento();
        agendamento.setCliente(cliente);
        agendamento.setProfissional(profissional);
        agendamento.setInicio(request.inicio());
        agendamento.setFim(request.fim());
        return agendamentoRepository.save(agendamento);
    }

    public List<Agendamento> listar() {
        return agendamentoRepository.findAll();
    }

    @Transactional
    public Agendamento cancelar(Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento não encontrado"));

        if (agendamento.getStatus() != StatusAgendamento.AGENDADO) {
            throw new RegraNegocioException("Só é possível cancelar agendamentos ativos");
        }

        agendamento.setStatus(StatusAgendamento.CANCELADO);
        return agendamento;
    }
}