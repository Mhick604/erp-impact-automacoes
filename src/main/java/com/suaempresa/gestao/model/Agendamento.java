package com.suaempresa.gestao.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity // Argumento: Diz ao Spring que esta classe vai virar uma tabela no banco de dados.
public class Agendamento {

    @Id // Argumento: Define que este campo é a chave principal (ID único) da tabela.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Argumento: Faz o ID ser gerado automaticamente (1, 2, 3...).
    private Long id;	

    @ManyToOne // Argumento: Significa "Muitos para Um". Muitos agendamentos podem pertencer a Um único cliente.
    @JoinColumn(name = "cliente_id") // Argumento: Cria uma coluna no banco que vai guardar apenas o número do ID do cliente.
    private Cliente cliente;
    private String tipoServico; // Ex: "Reparo Elétrico", "Instalação de Automação"
    private LocalDateTime dataHoraVisita;
    private Double valorEstimado;public Long getId() {
		return id;
		}
    @Enumerated(EnumType.STRING) // Argumento: Salva a palavra (ex: "PENDENTE") no banco de dados, em vez de um número (0, 1, 2).
    private StatusAgendamento status;  
 // --- MÓDULO FINANCEIRO ---
    private String statusPagamento = "PENDENTE"; // Pode ser "PENDENTE" ou "PAGO"
    private String metodoPagamento; // Pode ser "PIX", "CARTAO", "DINHEIRO"
    
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public String getTipoServico() {
		return tipoServico;
	}
	public void setTipoServico(String tipoServico) {
		this.tipoServico = tipoServico;
	}
	public LocalDateTime getDataHoraVisita() {
		return dataHoraVisita;
	}
	public void setDataHoraVisita(LocalDateTime dataHoraVisita) {
		this.dataHoraVisita = dataHoraVisita;
	}
	public Double getValorEstimado() {
		return valorEstimado;
	}
	public void setValorEstimado(Double valorEstimado) {
		this.valorEstimado = valorEstimado;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public StatusAgendamento getStatus() {
		return status;
	}
	public void setStatus(StatusAgendamento status) {
		this.status = status;
	}
	
	public String getStatusPagamento() {
        return statusPagamento;
    }

    public void setStatusPagamento(String statusPagamento) {
        this.statusPagamento = statusPagamento;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }
    
    
}