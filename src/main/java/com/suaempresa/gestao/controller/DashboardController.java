package com.suaempresa.gestao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.suaempresa.gestao.model.Lancamento;
import com.suaempresa.gestao.repository.ClienteRepository;
import com.suaempresa.gestao.repository.LancamentoRepository;
import com.suaempresa.gestao.repository.OrdemServicoRepository;

import java.util.List;

@Controller
public class DashboardController {

    @Autowired private ClienteRepository clienteRepo;
    @Autowired private OrdemServicoRepository osRepo;
    @Autowired private LancamentoRepository lancamentoRepo;

    @GetMapping("/")
    public String carregarDashboard(Model model) {
        // 1. Contar totais
        model.addAttribute("totalClientes", clienteRepo.count());
        model.addAttribute("totalOs", osRepo.count());

        // 2. Calcular o Financeiro (Filtra apenas o que já está PAGO)
        List<Lancamento> lancamentos = lancamentoRepo.findAll();
        
        double totalReceitas = lancamentos.stream()
            .filter(l -> "RECEITA".equals(l.getTipo()) && "PAGO".equals(l.getStatus()))
            .mapToDouble(Lancamento::getValor).sum();
            
        double totalDespesas = lancamentos.stream()
            .filter(l -> "DESPESA".equals(l.getTipo()) && "PAGO".equals(l.getStatus()))
            .mapToDouble(Lancamento::getValor).sum();

        // 3. O Lucro Real
        double caixaAtual = totalReceitas - totalDespesas;

        // Envia os números para o HTML
        model.addAttribute("totalReceitas", totalReceitas);
        model.addAttribute("totalDespesas", totalDespesas);
        model.addAttribute("caixaAtual", caixaAtual);

        return "index"; // Retorna a página inicial (index.html)
    }
}