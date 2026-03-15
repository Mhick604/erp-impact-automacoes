package com.suaempresa.gestao.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Empresa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeFantasia;
    private String cnpj;
    private String telefone;
    private String email;
    private String endereco;

    // --- OS CAMPOS DA LOGOMARCA ---
    
    // Guarda o arquivo da imagem em si
    @Basic(fetch = FetchType.LAZY) // Lazy = Só puxa a imagem do banco quando for realmente exibir
    @Column(columnDefinition = "BYTEA") // Salva como "arquivo" no PostgreSQL do Render
    private byte[] logo;
    
    // Guarda se a imagem é "image/png" ou "image/jpeg"
    private String tipoLogo;

    // ================================
    // GETTERS E SETTERS
    // ================================

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNomeFantasia() {
        return nomeFantasia;
    }
    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }
    public String getCnpj() {
        return cnpj;
    }
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getEndereco() {
        return endereco;
    }
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
    public byte[] getLogo() {
        return logo;
    }
    public void setLogo(byte[] logo) {
        this.logo = logo;
    }
    public String getTipoLogo() {
        return tipoLogo;
    }
    public void setTipoLogo(String tipoLogo) {
        this.tipoLogo = tipoLogo;
    }
}