package com.suaempresa.gestao.repository;

import com.suaempresa.gestao.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
}