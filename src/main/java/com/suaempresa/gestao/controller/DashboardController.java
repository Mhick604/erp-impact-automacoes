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
        // 1. LÓGICA FINANCEIRA (BLINDADA)
        // ==========================================
        List<Lancamento> lancamentos = lancamentoRepo.findAll();
        
        // Agora aceita "RECEITA" ou "ENTRADA", em qualquer formato (maiusculo/minusculo)
        double totalReceitas = lancamentos.stream()
            .filter(l -> l.getTipo() != null && 
                        (l.getTipo().equalsIgnoreCase("RECEITA") || l.getTipo().equalsIgnoreCase("ENTRADA")) && 
                        l.getStatus() != null && l.getStatus().equalsIgnoreCase("PAGO") && 
                        l.getValor() != null)
            .mapToDouble(Lancamento::getValor).sum();
            
        // Aceita "DESPESA" ou "SAIDA"
        double totalDespesas = lancamentos.stream()
            .filter(l -> l.getTipo() != null && 
                        (l.getTipo().equalsIgnoreCase("DESPESA") || l.getTipo().equalsIgnoreCase("SAIDA")) && 
                        l.getStatus() != null && l.getStatus().equalsIgnoreCase("PAGO") && 
                        l.getValor() != null)
            .mapToDouble(Lancamento::getValor).sum();

        double caixaAtual = totalReceitas - totalDespesas;

        model.addAttribute("caixaAtual", caixaAtual);
        model.addAttribute("totalClientes", clienteRepo.count());

        // ==========================================
        // 2. LÓGICA DA OPERAÇÃO E FATURAMENTO
        // ==========================================
        List<OrdemServico> todasAsOs = osRepo.findAll();
        
        long qtdAndamento = todasAsOs.stream()
                .filter(os -> os.getStatus() != null && 
                             (os.getStatus().equalsIgnoreCase("ABERTA") || os.getStatus().equalsIgnoreCase("EM_ANDAMENTO")))
                .count();
                
        long qtdConcluidas = todasAsOs.stream()
                .filter(os -> os.getStatus() != null && os.getStatus().equalsIgnoreCase("CONCLUIDA"))
                .count();
                
        // SOMA TODAS AS O.S. CONCLUÍDAS
        double totalOsConcluidas = todasAsOs.stream()
                .filter(os -> os.getStatus() != null && os.getStatus().equalsIgnoreCase("CONCLUIDA") && os.getValor() != null)
                .mapToDouble(OrdemServico::getValor)
                .sum();

        // MATEMÁTICA: O que tem de O.S. pronta MENOS o que já entrou de dinheiro no caixa
        double valorPendente = totalOsConcluidas - totalReceitas;
        
        // Impede que fique com saldo negativo no painel
        if (valorPendente < 0) {
            valorPendente = 0.0;
        }

        model.addAttribute("qtdAndamento", qtdAndamento);
        model.addAttribute("qtdConcluidas", qtdConcluidas);
        model.addAttribute("valorPendente", valorPendente);
        
        // Pega as últimas O.S. ativas para o Radar
        List<OrdemServico> osAtivas = todasAsOs.stream()
                .filter(os -> os.getStatus() != null && !os.getStatus().equalsIgnoreCase("CONCLUIDA"))
                .limit(5)
                .collect(Collectors.toList());
                
        model.addAttribute("ultimasOs", osAtivas);

        // ==========================================
        // 3. O.S. FINALIZADAS PARA NOTIFICAR NO WHATSAPP
        // ==========================================
        List<OrdemServico> ordensConcluidas = todasAsOs.stream()
                .filter(os -> os.getStatus() != null && os.getStatus().equalsIgnoreCase("CONCLUIDA"))
                .collect(Collectors.toList());
        model.addAttribute("ordensConcluidas", ordensConcluidas);

        return "index";
    }
}