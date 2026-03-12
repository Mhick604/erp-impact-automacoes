package com.suaempresa.gestao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.suaempresa.gestao.model.Usuario;
import com.suaempresa.gestao.repository.UsuarioRepository;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepo;
    
    @Autowired
    private PasswordEncoder passwordEncoder; // Nosso encriptador de senhas

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioRepo.findAll());
        return "usuarios"; // Vai chamar a tela HTML que vamos criar
    }

    @PostMapping("/salvar")
    public String salvarUsuario(Usuario formUsuario, @RequestParam(required = false) String novaSenha) {
        
        if (formUsuario.getId() != null) {
            // É UMA EDIÇÃO: Busca o usuário antigo no banco
            Usuario usuarioExistente = usuarioRepo.findById(formUsuario.getId()).orElse(null);
            
            if (usuarioExistente != null) {
                usuarioExistente.setNome(formUsuario.getNome());
                usuarioExistente.setLogin(formUsuario.getLogin());
                usuarioExistente.setRegra(formUsuario.getRegra()); // Aqui definimos se tem acesso ou não
                
                // Só troca a senha se você digitou alguma coisa no campo "Nova Senha"
                if (novaSenha != null && !novaSenha.trim().isEmpty()) {
                    usuarioExistente.setSenha(passwordEncoder.encode(novaSenha));
                }
                
                usuarioRepo.save(usuarioExistente);
            }
        } else {
            // É UM NOVO USUÁRIO
            formUsuario.setSenha(passwordEncoder.encode(novaSenha)); // Já criptografa logo de cara
            usuarioRepo.save(formUsuario);
        }
        
        return "redirect:/usuarios";
    }

    @GetMapping("/excluir/{id}")
    public String excluirUsuario(@PathVariable Long id) {
        // Cuidado: Excluir um usuário apaga o acesso dele.
        usuarioRepo.deleteById(id);
        return "redirect:/usuarios";
    }
}