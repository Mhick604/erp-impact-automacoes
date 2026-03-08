package com.suaempresa.gestao.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank; // Importante!

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // A regra entra aqui, logo acima da variável nome
    @NotBlank(message = "O nome do cliente não pode estar em branco")
    private String nome;

    // A regra entra aqui também
    @NotBlank(message = "O telefone é obrigatório")
    private String telefone;

    private String endereco; // Como o endereço pode ser opcional, não colocamos regra nele

    // ... (Seus Getters e Setters continuam aqui embaixo normalmente)

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

   
}