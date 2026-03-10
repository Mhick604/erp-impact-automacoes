package com.suaempresa.gestao.security; // Verifique se o pacote está correto

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.suaempresa.gestao.model.Usuario;
import com.suaempresa.gestao.repository.UsuarioRepository;

@Configuration
public class SetupInicial implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Verifica se o usuário já existe para não criar duplicado
        if (usuarioRepository.findByLogin("admin@impact.com").isEmpty()) {
            
            Usuario admin = new Usuario();
            admin.setNome("Administrador Impact");
            admin.setLogin("admin@impact.com"); // Esse é o E-MAIL de login
            
            // Aqui acontece a mágica: a senha "123456" vira um código secreto (BCrypt)
            admin.setSenha(passwordEncoder.encode("123456")); 
            
            admin.setRegra("ROLE_ADMIN"); // Define que ele é o chefe

            usuarioRepository.save(admin);
            System.out.println("✅ CHAVE MESTRA CRIADA: Usuário ADMIN salvo com sucesso!");
        }
    }
}