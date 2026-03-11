package com.suaempresa.gestao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.suaempresa.gestao.model.Tecnico;
import com.suaempresa.gestao.repository.TecnicoRepository;

@Controller
@RequestMapping("/tecnicos")
public class TecnicoController {

    @Autowired
    private TecnicoRepository repository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("tecnicos", repository.findAll());
        return "tecnicos"; 
    }

    @PostMapping("/salvar")
    public String salvar(Tecnico tecnico) {
        repository.save(tecnico);
        return "redirect:/tecnicos";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/tecnicos";
    }
}