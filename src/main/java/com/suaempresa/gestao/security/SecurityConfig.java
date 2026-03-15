package com.suaempresa.gestao.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desabilita proteção CSRF para facilitar nossos testes
            .authorizeHttpRequests(auth -> auth
                
                // 1. ACESSO PÚBLICO: Arquivos de design e tela de login precisam ser livres para todos!
                .requestMatchers("/login", "/error", "/css/**", "/js/**", "/img/**").permitAll()
                
                // 2. ACESSO DO TÉCNICO: A regra específica VEM ANTES da regra geral
                .requestMatchers("/ordens/tecnico/**", "/ordens/concluir-app/**").hasAnyRole("TECNICO", "ADMIN")
                
                // 3. ACESSO DO ADMINISTRADOR: Todo o resto do sistema
                .requestMatchers("/", "/clientes/**", "/empresa/**", "/tecnicos/**", "/ordens/**", "/financeiro/**", "/usuarios/**").hasRole("ADMIN")
                
                // 4. Qualquer outra rota exige login
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                
                // O GUARDA DE TRÂNSITO: Redirecionamento Inteligente!
                .successHandler((request, response, authentication) -> {
                    // Verifica se a pessoa que acabou de logar tem o cargo de TÉCNICO
                    boolean isTecnico = authentication.getAuthorities().stream()
                            .anyMatch(role -> role.getAuthority().equals("ROLE_TECNICO"));
                    
                    if (isTecnico) {
                        // Se for técnico, manda ele direto para o link do App!
                        // IMPORTANTE: Ajuste esse link abaixo para a URL exata da tela inicial do técnico
                        response.sendRedirect("/ordens/tecnico/painel"); 
                    } else {
                        // Se for Admin, manda para o Dashboard Principal
                        response.sendRedirect("/");
                    }
                })
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout") // Mostra a mensagem quando sair
                .permitAll()
            );

        return http.build();
    }

    // Cria o "Cofre" para criptografar as senhas no banco de dados
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configura o gerenciador oficial de autenticação
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}