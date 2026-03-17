package com.suaempresa.gestao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.suaempresa.gestao.model.Empresa;
import com.suaempresa.gestao.repository.EmpresaRepository;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private EmpresaRepository empresaRepository;

    // Isto injeta os dados da empresa em TODAS as páginas HTML automaticamente
    @ModelAttribute("empresaGlobal")
    public Empresa globalEmpresa() {
        return empresaRepository.findAll().stream().findFirst().orElse(new Empresa());
    }
}