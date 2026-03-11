package com.suaempresa.gestao.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.time.LocalDate;

@Entity
public class OrdemServico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;
    private Double valor;
    private String status = "PENDENTE"; // PENDENTE, ANDAMENTO, CONCLUIDA
    private LocalDate dataAbertura = LocalDate.now();

    // Uma OS tem apenas UM Cliente (Mas um cliente pode ter várias OS)
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    // Uma OS tem apenas UM Técnico responsável
    @ManyToOne
    @JoinColumn(name = "tecnico_id")
    private Tecnico tecnico;

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

    
    
}