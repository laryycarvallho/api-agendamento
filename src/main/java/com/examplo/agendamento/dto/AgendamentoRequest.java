package com.examplo.agendamento.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AgendamentoRequest(
        @NotNull Long clienteId,
        @NotNull Long profissionalId,
        @NotNull @Future LocalDateTime inicio,
        @NotNull LocalDateTime fim) {
}