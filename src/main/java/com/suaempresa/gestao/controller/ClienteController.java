package com.suaempresa.gestao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.suaempresa.gestao.model.Cliente;
import com.suaempresa.gestao.repository.ClienteRepository;

@Controller
@RequestMapping("/clientes") // Todas as rotas aqui começam com /clientes
public class ClienteController {

    @Autowired
    private ClienteRepository repository;

    // 1. Rota para abrir a página e listar os clientes
    @GetMapping
    public String listarClientes(Model model) {
        // Busca todos os clientes do banco e envia para a tela
        model.addAttribute("clientes", repository.findAll());
        return "clientes"; // Abre o arquivo templates/clientes.html
    }
    
    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/clientes";
    }

    // 2. Rota para salvar o novo cliente vindo do formulário
    @PostMapping("/salvar")
    public String salvarCliente(Cliente cliente) {
        // Salva no banco de dados (H2 local ou Postgres na Render)
        repository.save(cliente);
        
        // Redireciona de volta para a lista atualizada
        return "redirect:/clientes";
    }
}