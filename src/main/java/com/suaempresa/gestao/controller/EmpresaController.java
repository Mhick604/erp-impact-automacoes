package com.suaempresa.gestao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.suaempresa.gestao.model.Empresa;
import com.suaempresa.gestao.repository.EmpresaRepository;

@Controller
@RequestMapping("/empresa")
public class EmpresaController {

    @Autowired
    private EmpresaRepository repository;

    @GetMapping
    public String carregarConfiguracoes(Model model) {
        // Pega a primeira empresa do banco. Se não existir, cria uma "vazia" para a tela não dar erro.
        Empresa minhaEmpresa = repository.findAll().stream().findFirst().orElse(new Empresa());
        model.addAttribute("empresa", minhaEmpresa);
        return "empresa"; 
    }

    @PostMapping("/salvar")
    public String salvar(Empresa empresa) {
        repository.save(empresa);
        return "redirect:/empresa";
    }
}