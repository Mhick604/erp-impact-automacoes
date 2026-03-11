package com.suaempresa.gestao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.suaempresa.gestao.model.Lancamento;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
}