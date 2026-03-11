package com.suaempresa.gestao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.suaempresa.gestao.model.Empresa;
import com.suaempresa.gestao.model.OrdemServico;
import com.suaempresa.gestao.repository.ClienteRepository;
import com.suaempresa.gestao.repository.EmpresaRepository;
import com.suaempresa.gestao.repository.OrdemServicoRepository;
import com.suaempresa.gestao.repository.TecnicoRepository;

@Controller
@RequestMapping("/ordens")
public class OrdemServicoController {
	
    @Autowired private OrdemServicoRepository osRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private TecnicoRepository tecnicoRepository;
    @Autowired private EmpresaRepository empresaRepository;
    
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("ordens", osRepository.findAll());
        model.addAttribute("clientes", clienteRepository.findAll());
        model.addAttribute("tecnicos", tecnicoRepository.findAll());
        return "ordens"; 
    }

    @PostMapping("/salvar")
    public String salvar(OrdemServico os) {
        osRepository.save(os);
        return "redirect:/ordens";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        osRepository.deleteById(id);
        return "redirect:/ordens";
    }
    
 // Rota para gerar a O.S. em tela cheia para PDF
    @GetMapping("/imprimir/{id}")
    public String imprimirOS(@PathVariable Long id, Model model) {
        // Busca a O.S. específica
        OrdemServico os = osRepository.findById(id).orElse(null);
        if(os == null) {
            return "redirect:/ordens"; // Se não achar, volta pra lista
        }
        
        // Busca os dados da Impact Automações para o cabeçalho
        Empresa empresa = empresaRepository.findAll().stream().findFirst().orElse(new Empresa());
        
        model.addAttribute("os", os);
        model.addAttribute("empresa", empresa);
        
        return "os-imprimir"; // Vai abrir a tela limpa
    }
}