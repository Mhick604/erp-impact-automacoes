package com.suaempresa.gestao.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.suaempresa.gestao.model.Agendamento;
import com.suaempresa.gestao.model.StatusAgendamento;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    
    // O filtro que você já fez:
    List<Agendamento> findByStatus(StatusAgendamento status);
    
    // O NOVO FILTRO: O Spring lê "Between" e entende que deve buscar entre duas datas!
    List<Agendamento> findByDataHoraVisitaBetween(LocalDateTime dataInicio, LocalDateTime dataFim);
}