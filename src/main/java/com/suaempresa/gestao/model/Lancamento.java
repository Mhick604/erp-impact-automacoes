package com.suaempresa.gestao.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;

@Entity
public class Lancamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao; // Ex: Compra de Cabos, Recebimento O.S. #1
    private Double valor;
    
    private String tipo; // RECEITA ou DESPESA
    private String status = "PENDENTE"; // PAGO ou PENDENTE
    
    private LocalDate data = LocalDate.now();

    // Lembre-se de gerar os Getters e Setters!
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
}