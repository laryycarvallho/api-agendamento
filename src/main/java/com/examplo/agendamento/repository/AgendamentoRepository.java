package com.examplo.agendamento.repository;

import com.examplo.agendamento.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    @Query("""
            select count(a) > 0 from Agendamento a
            where a.profissional.id = :profissionalId
              and a.status = com.examplo.agendamento.model.StatusAgendamento.AGENDADO
              and a.inicio < :fim and a.fim > :inicio
            """)
    boolean existeConflito(@Param("profissionalId") Long profissionalId,
                           @Param("inicio") LocalDateTime inicio,
                           @Param("fim") LocalDateTime fim);
}