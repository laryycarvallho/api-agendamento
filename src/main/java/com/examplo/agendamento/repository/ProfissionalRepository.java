package com.examplo.agendamento.repository;

import com.examplo.agendamento.model.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
}