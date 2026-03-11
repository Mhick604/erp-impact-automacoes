package com.suaempresa.gestao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.suaempresa.gestao.model.Lancamento;
import com.suaempresa.gestao.repository.LancamentoRepository;

@Controller
@RequestMapping("/financeiro")
public class FinanceiroController {

    @Autowired
    private LancamentoRepository repository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("lancamentos", repository.findAll());
        return "financeiro"; 
    }

    @PostMapping("/salvar")
    public String salvar(Lancamento lancamento) {
        repository.save(lancamento);
        return "redirect:/financeiro";
    }

    // Botão mágico para dar "Baixa" (Marcar como PAGO)
    @GetMapping("/pagar/{id}")
    public String pagar(@PathVariable Long id) {
        Lancamento l = repository.findById(id).orElse(null);
        if(l != null) {
            l.setStatus("PAGO");
            repository.save(l);
        }
        return "redirect:/financeiro";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/financeiro";
    }
}