package com.suaempresa.gestao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.suaempresa.gestao.model.Usuario;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByLogin(String login);
}