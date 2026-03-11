package com.suaempresa.gestao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.suaempresa.gestao.model.Tecnico;

@Repository
public interface TecnicoRepository extends JpaRepository<Tecnico, Long> {
}