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
    public String excluir(@PathVariable Long id, org.springframework.web.servlet.mvc.support.RedirectAttributes attributes) {
        try {
            // 🔥 CORRIGIDO AQUI: Usando apenas "repository" para combinar com a sua linha 18
            repository.deleteById(id);
            attributes.addFlashAttribute("mensagemSucesso", "Técnico excluído com sucesso!");
            
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // Se o banco de dados chiar por causa de uma O.S. vinculada, ele cai aqui!
            attributes.addFlashAttribute("mensagemErro", "Acesso Negado: Você não pode excluir este técnico porque ele já possui Ordens de Serviço no histórico da empresa.");
            
        } catch (Exception e) {
            // Qualquer outro erro bizarro cai aqui
            attributes.addFlashAttribute("mensagemErro", "Ocorreu um erro inesperado ao tentar excluir.");
        }
        
        
        return "redirect:/tecnicos";
    }
}