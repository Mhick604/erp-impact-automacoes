package com.suaempresa.gestao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suaempresa.gestao.model.Cliente;
import com.suaempresa.gestao.repository.ClienteRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository repository;

    @GetMapping
    public List<Cliente> listarTodos() {
        return repository.findAll();
    }

    @PostMapping
    public Cliente criarCliente(@Valid @RequestBody Cliente novoCliente) {
        return repository.save(novoCliente);
    }
    
 // 1. Buscar um cliente específico pelo ID
    @GetMapping("/{id}")
    public Cliente buscarPorId(@PathVariable("id") Long id) {
        return repository.findById(id).orElse(null);
    }

    // 2. Atualizar um cliente existente
    @PutMapping("/{id}")
    public Cliente atualizarCliente(@PathVariable("id") Long id, @RequestBody Cliente clienteModificado) {
        clienteModificado.setId(id); // Garante que estamos alterando o cliente certo
        return repository.save(clienteModificado);
    }

    // 3. Deletar um cliente
    @DeleteMapping("/{id}")
    public void deletarCliente(@PathVariable("id") Long id) {
        repository.deleteById(id);
    }
}