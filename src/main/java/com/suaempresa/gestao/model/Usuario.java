
package com.suaempresa.gestao.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String login; 
    private String senha;
    private String regra; 

    public Usuario() {}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Mapeia a regra do banco (Ex: ROLE_ADMIN) para o Spring Security
        return List.of(new SimpleGrantedAuthority(regra));
    }

    @Override
    public String getPassword() { return this.senha; }
    
    @Override
    public String getUsername() { return this.login; }
    
    @Override
    public boolean isAccountNonExpired() { return true; }
    
    @Override
    public boolean isAccountNonLocked() { return true; }
    
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    
    @Override
    public boolean isEnabled() { 
        // 🛡️ A MÁGICA AQUI: Se o chefe bloqueou, não deixa nem fazer login!
        if ("ROLE_BLOQUEADO".equals(this.regra)) {
            return false;
        }
        return true; 
    }

    // --- GETTERS E SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    
    public String getRegra() { return regra; }
    public void setRegra(String regra) { this.regra = regra; }
}