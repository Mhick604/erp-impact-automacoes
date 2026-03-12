package com.suaempresa.gestao.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;

@Entity
public class FotosOS {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // A mágica: @Lob avisa ao banco de dados que isso é um arquivo grande (a foto em si)
    @Lob
    private byte[] dadosImagem; 
    
    private String tipoArquivo; // Ex: "image/jpeg" ou "image/png"

    // Dizemos que muitas fotos pertencem a UMA Ordem de Serviço
    @ManyToOne
    @JoinColumn(name = "ordem_servico_id")
    private OrdemServico ordemServico;

    // --- GETTERS E SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public byte[] getDadosImagem() { return dadosImagem; }
    public void setDadosImagem(byte[] dadosImagem) { this.dadosImagem = dadosImagem; }
    public String getTipoArquivo() { return tipoArquivo; }
    public void setTipoArquivo(String tipoArquivo) { this.tipoArquivo = tipoArquivo; }
    public OrdemServico getOrdemServico() { return ordemServico; }
    public void setOrdemServico(OrdemServico ordemServico) { this.ordemServico = ordemServico; }
}