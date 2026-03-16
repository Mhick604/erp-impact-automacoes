package com.suaempresa.gestao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.suaempresa.gestao.model.ItemEstoque;
import com.suaempresa.gestao.repository.ItemEstoqueRepository;

@Controller
@RequestMapping("/estoque")
public class EstoqueController {

    @Autowired
    private ItemEstoqueRepository repository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("itens", repository.findAll());
        return "estoque";
    }

    @PostMapping("/salvar")
    public String salvar(ItemEstoque item) {
        repository.save(item);
        return "redirect:/estoque";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/estoque";
    }
}