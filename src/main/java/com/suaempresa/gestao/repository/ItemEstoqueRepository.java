package com.suaempresa.gestao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.suaempresa.gestao.model.ItemEstoque;

public interface ItemEstoqueRepository extends JpaRepository<ItemEstoque, Long> {
}