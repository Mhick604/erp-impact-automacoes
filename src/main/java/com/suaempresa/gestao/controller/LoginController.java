package com.suaempresa.gestao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String carregarTelaLogin() {
        return "login"; // Vai procurar o arquivo login.html na pasta templates
    }
}