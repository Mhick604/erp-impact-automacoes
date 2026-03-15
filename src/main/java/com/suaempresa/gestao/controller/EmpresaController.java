package com.suaempresa.gestao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.suaempresa.gestao.model.Empresa;
import com.suaempresa.gestao.repository.EmpresaRepository;

@Controller
@RequestMapping("/empresa")
public class EmpresaController {

    @Autowired
    private EmpresaRepository repository;

    @GetMapping
    public String carregarConfiguracoes(Model model) {
        Empresa minhaEmpresa = repository.findAll().stream().findFirst().orElse(new Empresa());
        model.addAttribute("empresa", minhaEmpresa);
        return "empresa"; 
    }

    // O SALVAR TURBINADO (Agora aceita arquivos)
    @PostMapping("/salvar")
    public String salvar(Empresa empresa, @RequestParam(value = "arquivoLogo", required = false) MultipartFile arquivo) {
        try {
            // Escudo: Se a empresa já existir, não apaga a logo velha se o cara não enviou uma nova
            if (empresa.getId() != null) {
                Empresa empresaAntiga = repository.findById(empresa.getId()).orElse(null);
                if (empresaAntiga != null) {
                    // Se o usuário clicou em salvar sem escolher foto, mantemos a que já estava no banco!
                    if (arquivo == null || arquivo.isEmpty()) {
                        empresa.setLogo(empresaAntiga.getLogo());
                        empresa.setTipoLogo(empresaAntiga.getTipoLogo());
                    }
                }
            }

            // Se o usuário mandou uma foto nova, nós convertemos para Bytes e colocamos na Empresa
            if (arquivo != null && !arquivo.isEmpty()) {
                empresa.setLogo(arquivo.getBytes());
                empresa.setTipoLogo(arquivo.getContentType());
            }

            repository.save(empresa);

        } catch (Exception e) {
            System.out.println("Erro ao salvar logotipo: " + e.getMessage());
        }

        return "redirect:/empresa";
    }

    // ==========================================
    // 🖼️ O CANO DE SAÍDA: MOSTRA A LOGO NA TELA
    // ==========================================
    @GetMapping("/logo")
    public ResponseEntity<byte[]> exibirLogo() {
        // Pega a empresa
        Empresa empresa = repository.findAll().stream().findFirst().orElse(null);
        
        // Se ela tiver uma foto salva...
        if (empresa != null && empresa.getLogo() != null) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, empresa.getTipoLogo()) // Avisa o navegador se é PNG ou JPG
                    .body(empresa.getLogo()); // Cospe a imagem na tela!
        }
        
        // Se não tiver, dá Erro 404 (fica aquele ícone de imagem quebrada)
        return ResponseEntity.notFound().build();
    }
}