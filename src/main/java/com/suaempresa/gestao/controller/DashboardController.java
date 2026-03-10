package com.suaempresa.gestao.controller;

import org.springframework.stereotype.Controller; // VOLTOU PARA CONTROLLER
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/")
    public String abrirDashboard() {
        return "index"; // Ele vai buscar o index.html na pasta templates
    }
}