package com.suaempresa.gestao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.suaempresa.gestao.model.Lancamento;
import com.suaempresa.gestao.model.OrdemServico;
import com.suaempresa.gestao.repository.ClienteRepository;
import com.suaempresa.gestao.repository.LancamentoRepository;
import com.suaempresa.gestao.repository.OrdemServicoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    @Autowired private ClienteRepository clienteRepo;
    @Autowired private OrdemServicoRepository osRepo;
    @Autowired private LancamentoRepository lancamentoRepo;

    @GetMapping("/")
    public String carregarDashboard(Model model) {
        
        // ==========================================
        // 1. LÓGICA FINANCEIRA (A sua obra-prima)
        // ==========================================
        List<Lancamento> lancamentos = lancamentoRepo.findAll();
        
        double totalReceitas = lancamentos.stream()
            .filter(l -> "RECEITA".equals(l.getTipo()) && "PAGO".equals(l.getStatus()))
            .mapToDouble(Lancamento::getValor).sum();
            
        double totalDespesas = lancamentos.stream()
            .filter(l -> "DESPESA".equals(l.getTipo()) && "PAGO".equals(l.getStatus()))
            .mapToDouble(Lancamento::getValor).sum();

        double caixaAtual = totalReceitas - totalDespesas;

        // Envia para a tela
        model.addAttribute("caixaAtual", caixaAtual);
        model.addAttribute("totalClientes", clienteRepo.count());

        // ==========================================
        // 2. LÓGICA DA OPERAÇÃO (Radar e O.S.)
        // ==========================================
        List<OrdemServico> todasAsOs = osRepo.findAll();
        
        long qtdAndamento = todasAsOs.stream()
                .filter(os -> "ABERTA".equals(os.getStatus()) || "EM_ANDAMENTO".equals(os.getStatus()))
                .count();
                
        long qtdConcluidas = todasAsOs.stream()
                .filter(os -> "CONCLUIDA".equals(os.getStatus()))
                .count();
                
        double valorPendente = todasAsOs.stream()
                .filter(os -> "CONCLUIDA".equals(os.getStatus()))
                .mapToDouble(OrdemServico::getValor)
                .sum();

        // Envia para a tela
        model.addAttribute("qtdAndamento", qtdAndamento);
        model.addAttribute("qtdConcluidas", qtdConcluidas);
        model.addAttribute("valorPendente", valorPendente);
        
        // Pega as últimas O.S. que não estão concluídas para o Radar de Técnicos
        List<OrdemServico> osAtivas = todasAsOs.stream()
                .filter(os -> !"CONCLUIDA".equals(os.getStatus()))
                .limit(5)
                .collect(Collectors.toList());
                
        model.addAttribute("ultimasOs", osAtivas);

        return "index";
    }
}