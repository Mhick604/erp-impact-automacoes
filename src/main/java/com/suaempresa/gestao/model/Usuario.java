package com.suaempresa.gestao.model; // Atenção: mantenha o nome do SEU pacote aqui no topo

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String login; // Vai ser o e-mail ou nome de usuário
    private String senha;
    private String regra; // Para separar quem é "ADMIN" (Chefe) de "USER" (Funcionário)

    // Construtor vazio (obrigatório para o banco de dados)
    public Usuario() {
    }

    // Getters e Setters
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