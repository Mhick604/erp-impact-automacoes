package com.suaempresa.gestao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.suaempresa.gestao.model.Lancamento;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {// Soma Receitas Pagas
    @org.springframework.data.jpa.repository.Query("SELECT SUM(l.valor) FROM Lancamento l WHERE l.tipo = 'RECEITA' AND l.status = 'PAGO'")
    Double sumReceitasPagas();

    // Soma Despesas Pagas
    @org.springframework.data.jpa.repository.Query("SELECT SUM(l.valor) FROM Lancamento l WHERE l.tipo = 'DESPESA' AND l.status = 'PAGO'")
    Double sumDespesasPagas();

    // Soma Receitas Pendentes (Aguardando Faturamento)
    @org.springframework.data.jpa.repository.Query("SELECT SUM(l.valor) FROM Lancamento l WHERE l.tipo = 'RECEITA' AND l.status = 'PENDENTE'")
    Double sumReceitasPendentes();
}