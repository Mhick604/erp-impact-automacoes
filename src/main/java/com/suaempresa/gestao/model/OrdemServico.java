package com.suaempresa.gestao.model;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType; 
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class OrdemServico {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;
    private Double valor;
    private String status = "PENDENTE"; // PENDENTE, ANDAMENTO, CONCLUIDA
    private LocalDate dataAbertura = LocalDate.now();
    private String tipo; // Vai guardar se é "ORÇAMENTO" ou "OS DEFINITIVA"
    private String codigoImovel; // Ex: "Cód. 3344"
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tecnico_id")
    private Tecnico tecnico;
    
    // --- CAMPOS (UPGRADE IMOBILIÁRIA) ---
    @Column(columnDefinition = "TEXT")
    private String problemasIdentificados;
    
    @Column(columnDefinition = "TEXT")
    private String servicoRealizado;
    
    @Column(columnDefinition = "TEXT")
    private String observacoes;
    
    private java.time.LocalDate dataFinalizacao;

    // --- A LISTA DE FOTOS ---
    @OneToMany(mappedBy = "ordemServico", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private java.util.List<FotosOS> fotos = new java.util.ArrayList<>();

    // 🔥 NOVOS CAMPOS PARA NOTA FISCAL E ESTOQUE 🔥
    private String numeroNotaFiscal;
    private byte[] arquivoNotaFiscal; 
    private String tipoArquivoNotaFiscal;

    @OneToMany(mappedBy = "ordemServico", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private java.util.List<MaterialUtilizado> materiaisUtilizados = new java.util.ArrayList<>();

    // ==========================================
    // GETTERS E SETTERS
    // ==========================================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getDataAbertura() { return dataAbertura; }
    public void setDataAbertura(LocalDate dataAbertura) { this.dataAbertura = dataAbertura; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Tecnico getTecnico() { return tecnico; }
    public void setTecnico(Tecnico tecnico) { this.tecnico = tecnico; }
    public java.time.LocalDate getDataFinalizacao() { return dataFinalizacao; }
    public void setDataFinalizacao(java.time.LocalDate dataFinalizacao) { this.dataFinalizacao = dataFinalizacao; }
    public String getCodigoImovel() { return codigoImovel; }
    public void setCodigoImovel(String codigoImovel) { this.codigoImovel = codigoImovel; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getProblemasIdentificados() { return problemasIdentificados; }
    public void setProblemasIdentificados(String problemasIdentificados) { this.problemasIdentificados = problemasIdentificados; }
    public String getServicoRealizado() { return servicoRealizado; }
    public void setServicoRealizado(String servicoRealizado) { this.servicoRealizado = servicoRealizado; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public java.util.List<FotosOS> getFotos() { return fotos; }
    public void setFotos(java.util.List<FotosOS> fotos) { this.fotos = fotos; }

    // Getters/Setters dos campos novos
    public String getNumeroNotaFiscal() { return numeroNotaFiscal; }
    public void setNumeroNotaFiscal(String numeroNotaFiscal) { this.numeroNotaFiscal = numeroNotaFiscal; }
    public byte[] getArquivoNotaFiscal() { return arquivoNotaFiscal; }
    public void setArquivoNotaFiscal(byte[] arquivoNotaFiscal) { this.arquivoNotaFiscal = arquivoNotaFiscal; }
    public String getTipoArquivoNotaFiscal() { return tipoArquivoNotaFiscal; }
    public void setTipoArquivoNotaFiscal(String tipoArquivoNotaFiscal) { this.tipoArquivoNotaFiscal = tipoArquivoNotaFiscal; }
    public java.util.List<MaterialUtilizado> getMateriaisUtilizados() { return materiaisUtilizados; }
    public void setMateriaisUtilizados(java.util.List<MaterialUtilizado> materiaisUtilizados) { this.materiaisUtilizados = materiaisUtilizados; }
}