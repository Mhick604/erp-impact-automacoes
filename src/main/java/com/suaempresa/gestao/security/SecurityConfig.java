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
            .csrf(csrf -> csrf.disable()) // Desabilita proteção CSRF (Útil para testes agora)
            .authorizeHttpRequests(auth -> auth
                // 1. A PORTA DA FRENTE (A CORREÇÃO ESTÁ AQUI): 
                // O Spring precisa saber que a página /login é pública, senão ele fica te redirecionando em loop infinito.
            		.requestMatchers("/login", "/error", "/css/**", "/js/**", "/img/**").permitAll()
                
                // 2. O RESTO DO SISTEMA (FECHADO A SETE CHAVES):
                .requestMatchers("/clientes/**", "/empresa/**", "/tecnicos/**", "/ordens/**", "/financeiro/**").authenticated()
                .anyRequest().authenticated() // Qualquer outra página também precisa de login
            )
            .formLogin(form -> form
                .loginPage("/login") // Diz ao Spring qual é a nossa tela HTML bonita
                .defaultSuccessUrl("/", true) // Se a senha estiver certa, vai direto pro Dashboard (index.html)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout") // Mostra a mensagem verde quando sair
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