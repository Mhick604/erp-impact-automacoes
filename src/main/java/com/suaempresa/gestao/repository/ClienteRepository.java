package com.suaempresa.gestao.repository;

import com.suaempresa.gestao.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Não precisa escrever nada aqui, o JpaRepository já faz a mágica!
}