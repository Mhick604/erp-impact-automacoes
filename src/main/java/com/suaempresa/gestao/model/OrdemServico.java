package com.suaempresa.gestao.model;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType; // <-- IMPORTAÇÃO NOVA AQUI
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
    

    // Uma OS tem apenas UM Cliente (Mas um cliente pode ter várias OS)
    // ADICIONADO: fetch = FetchType.EAGER para não dar erro na nuvem
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    // Uma OS tem apenas UM Técnico responsável
    // ADICIONADO: fetch = FetchType.EAGER
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tecnico_id")
    private Tecnico tecnico;
    
    // --- NOVOS CAMPOS (UPGRADE IMOBILIÁRIA) ---
    
    @Column(columnDefinition = "TEXT")
    private String problemasIdentificados; // TEXT permite textos gigantes
    
    @Column(columnDefinition = "TEXT")
    private String servicoRealizado;
    
    @Column(columnDefinition = "TEXT")
    private String observacoes;
    
    private java.time.LocalDate dataFinalizacao;

    // --- A LISTA DE FOTOS ---
    // ADICIONADO: fetch = FetchType.EAGER (O verdadeiro antídoto do Erro 500)
    @OneToMany(mappedBy = "ordemServico", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private java.util.List<FotosOS> fotos = new java.util.ArrayList<>();


    // ==========================================
    // GETTERS E SETTERS (Mantidos intocáveis)
    // ==========================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDate dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Tecnico getTecnico() {
        return tecnico;
    }

    public void setTecnico(Tecnico tecnico) {
        this.tecnico = tecnico;
    }

    public java.time.LocalDate getDataFinalizacao() {
        return dataFinalizacao;
    }

    public void setDataFinalizacao(java.time.LocalDate dataFinalizacao) {
        this.dataFinalizacao = dataFinalizacao;
    }

    public String getCodigoImovel() {
        return codigoImovel;
    }

    public void setCodigoImovel(String codigoImovel) {
        this.codigoImovel = codigoImovel;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getProblemasIdentificados() {
        return problemasIdentificados;
    }

    public void setProblemasIdentificados(String problemasIdentificados) {
        this.problemasIdentificados = problemasIdentificados;
    }

    public String getServicoRealizado() {
        return servicoRealizado;
    }

    public void setServicoRealizado(String servicoRealizado) {
        this.servicoRealizado = servicoRealizado;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public java.util.List<FotosOS> getFotos() {
        return fotos;
    }

    public void setFotos(java.util.List<FotosOS> fotos) {
        this.fotos = fotos;
    }
}