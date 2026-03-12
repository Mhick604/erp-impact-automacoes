package com.suaempresa.gestao;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.suaempresa.gestao.model.Usuario;
import com.suaempresa.gestao.repository.UsuarioRepository;

@SpringBootApplication
public class ImpactGestaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImpactGestaoApplication.class, args);
    }

    // O "Robô de Inicialização" fica aqui, fora do main, mas dentro da classe
    @Bean
    CommandLineRunner init(UsuarioRepository usuarioRepo, PasswordEncoder passwordEncoder) {
        return args -> {
            if (usuarioRepo.count() == 0) {
                // 1. CRIANDO O CHEFE (ADMIN)
                Usuario admin = new Usuario();
                admin.setNome("Mateus Admin");
                admin.setLogin("admin");
                admin.setSenha(passwordEncoder.encode("123")); 
                admin.setRegra("ROLE_ADMIN");
                usuarioRepo.save(admin);

                // 2. CRIANDO O TÉCNICO (CAMPO)
                Usuario tecnico = new Usuario();
                tecnico.setNome("João Técnico");
                tecnico.setLogin("joao");
                tecnico.setSenha(passwordEncoder.encode("123")); 
                tecnico.setRegra("ROLE_TECNICO");
                usuarioRepo.save(tecnico);

                System.out.println("✅ USUÁRIOS DE TESTE CRIADOS!");
                System.out.println("👉 Admin: admin / 123");
                System.out.println("👉 Técnico: joao / 123");
            }
        };
    }
}