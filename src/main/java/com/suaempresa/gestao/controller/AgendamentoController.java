package com.suaempresa.gestao.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.suaempresa.gestao.model.Agendamento;
import com.suaempresa.gestao.model.StatusAgendamento;
import com.suaempresa.gestao.repository.AgendamentoRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoRepository repository;

    @GetMapping
    public List<Agendamento> listarTodos() {
        return repository.findAll();
    }

    @PostMapping
    public Agendamento criarAgendamento(@Valid @RequestBody Agendamento novoAgendamento) {
        return repository.save(novoAgendamento);
    }

    // --- OS NOVOS PODERES COMEÇAM AQUI ---

    // Objetivo: Atualizar um agendamento existente (Ex: Mudar o valor ou a data)
    @PutMapping("/{id}")
    public Agendamento atualizarAgendamento(@PathVariable("id") Long id, @RequestBody Agendamento agendamentoModificado) {
        // O @PathVariable pega o ID que vem na URL (ex: /api/agendamentos/1)
        agendamentoModificado.setId(id); // Garante que estamos alterando o registro certo
        return repository.save(agendamentoModificado); // O save() atualiza se o ID já existir
    }
    

    // Objetivo: Deletar um agendamento (Ex: Cliente cancelou)
    @DeleteMapping("/{id}")
    public void deletarAgendamento(@PathVariable("id") Long id) {
        repository.deleteById(id); // Comando automático do banco para apagar pelo ID
    }
    
 // Buscar um agendamento específico pelo ID (Necessário para o botão de Pagamento!)
    @GetMapping("/{id}")
    public Agendamento buscarPorId(@PathVariable("id") Long id) {
        return repository.findById(id).orElse(null);
    }
    
 // Objetivo: Buscar agendamentos dentro de um período específico
    @GetMapping("/periodo")
    public List<Agendamento> buscarPorPeriodo(
            @RequestParam("inicio") LocalDateTime inicio,
            @RequestParam("fim") LocalDateTime fim) {
        
        return repository.findByDataHoraVisitaBetween(inicio, fim);
    }
    
}