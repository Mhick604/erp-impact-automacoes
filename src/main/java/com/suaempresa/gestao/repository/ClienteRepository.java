package com.suaempresa.gestao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.suaempresa.gestao.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}