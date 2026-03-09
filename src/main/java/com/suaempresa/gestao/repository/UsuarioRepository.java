package com.suaempresa.gestao.repository; // Mantenha o seu pacote original aqui

import org.springframework.data.jpa.repository.JpaRepository;
import com.suaempresa.gestao.model.Usuario; // Importe a classe que criamos
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Essa é a linha mágica que vai buscar o usuário no banco para fazer o login!
    Optional<Usuario> findByLogin(String login);
}