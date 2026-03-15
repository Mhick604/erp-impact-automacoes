package com.suaempresa.gestao.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.suaempresa.gestao.model.Empresa;
import com.suaempresa.gestao.model.FotosOS;
import com.suaempresa.gestao.model.Lancamento;
import com.suaempresa.gestao.model.OrdemServico;
import com.suaempresa.gestao.repository.ClienteRepository;
import com.suaempresa.gestao.repository.EmpresaRepository;
import com.suaempresa.gestao.repository.LancamentoRepository;
import com.suaempresa.gestao.repository.OrdemServicoRepository;
import com.suaempresa.gestao.repository.TecnicoRepository;

@Controller
@RequestMapping("/ordens")
public class OrdemServicoController {
    
    @Autowired 
    private OrdemServicoRepository osRepository;
    
    @Autowired 
    private ClienteRepository clienteRepository;
    
    @Autowired 
    private TecnicoRepository tecnicoRepository;
    
    @Autowired 
    private EmpresaRepository empresaRepository;
    
    @Autowired 
    private LancamentoRepository lancamentoRepo;
    
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("ordens", osRepository.findAll());
        model.addAttribute("clientes", clienteRepository.findAll());
        model.addAttribute("tecnicos", tecnicoRepository.findAll());
        return "ordens"; 
    }

    @PostMapping("/salvar")
    public String salvarOS(OrdemServico ordemServico, 
                           @RequestParam(value = "arquivosFotos", required = false) MultipartFile[] arquivos) {
                try {
                    // Se a OS já tem um ID, significa que é uma EDIÇÃO
                    if (ordemServico.getId() != null) {
                        OrdemServico osAntiga = osRepository.findById(ordemServico.getId()).orElse(null);
                        if (osAntiga != null) {
                            ordemServico.setFotos(osAntiga.getFotos()); 
                        }
                    }

                    if (arquivos != null && arquivos.length > 0) {
                        for (MultipartFile arquivo : arquivos) {
                            if (!arquivo.isEmpty()) {
                                FotosOS novaFoto = new FotosOS();
                                novaFoto.setDadosImagem(arquivo.getBytes());
                                novaFoto.setTipoArquivo(arquivo.getContentType());
                                novaFoto.setOrdemServico(ordemServico);
                                ordemServico.getFotos().add(novaFoto);
                            }
                        }
                    }
                    
                    osRepository.save(ordemServico);
                    
                    // GATILHO FINANCEIRO
                    if ("CONCLUIDA".equals(ordemServico.getStatus())) {
                        String identificador = "O.S. #" + ordemServico.getId();
                        List<Lancamento> todosLancamentos = lancamentoRepo.findAll();
                        boolean jaFoiCobrado = todosLancamentos.stream()
                                .anyMatch(l -> l.getDescricao() != null && l.getDescricao().contains(identificador));
                        
                        if (!jaFoiCobrado) {
                            Lancamento novaReceita = new Lancamento();
                            String nomeCliente = (ordemServico.getCliente() != null) ? ordemServico.getCliente().getNome() : "Cliente não informado";
                            novaReceita.setDescricao("Receita " + identificador + " - " + nomeCliente);
                            novaReceita.setValor(ordemServico.getValor());
                            novaReceita.setTipo("RECEITA");
                            novaReceita.setStatus("PENDENTE");
                            novaReceita.setData(LocalDate.now());
                            lancamentoRepo.save(novaReceita);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Erro ao salvar a OS: " + e.getMessage());
                }
        return "redirect:/ordens";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        osRepository.deleteById(id);
        return "redirect:/ordens";
    }
    
    @GetMapping("/imprimir/{id}")
    public String imprimirOS(@PathVariable Long id, Model model) {
        OrdemServico os = osRepository.findById(id).orElse(null);
        if(os == null) {
            return "redirect:/ordens";
        }
        Empresa empresa = empresaRepository.findAll().stream().findFirst().orElse(new Empresa());
        model.addAttribute("os", os);
        model.addAttribute("empresa", empresa);
        return "os-imprimir";
    }
    
    @GetMapping("/foto/{idOs}/{indice}")
    public ResponseEntity<byte[]> exibirFoto(@PathVariable Long idOs, @PathVariable int indice) {
        Optional<OrdemServico> osOptional = osRepository.findById(idOs);
        if (osOptional.isPresent()) {
            OrdemServico os = osOptional.get();
            if (os.getFotos() != null && os.getFotos().size() > indice) {
                FotosOS foto = os.getFotos().get(indice);
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, foto.getTipoArquivo())
                        .body(foto.getDadosImagem());
            }
        }
        return ResponseEntity.notFound().build();
    }
    
    // ==========================================
    // 📱 1. A TELA DO APP 
    // ==========================================
    @GetMapping("/tecnico/{tecnicoId}")
    public String portalTecnico(@PathVariable Long tecnicoId, Model model) {
        com.suaempresa.gestao.model.Tecnico tecnico = tecnicoRepository.findById(tecnicoId).orElse(null);
        
        List<OrdemServico> osDoTecnico = osRepository.findAll().stream()
            .filter(os -> os.getTecnico() != null && os.getTecnico().getId().equals(tecnicoId))
            .filter(os -> !"CONCLUIDA".equals(os.getStatus()))
            .toList();

        model.addAttribute("ordens", osDoTecnico);
        model.addAttribute("tecnico", tecnico);
        
        // Pega a empresa para o botão de WhatsApp do Escritório funcionar no App
        model.addAttribute("empresa", empresaRepository.findAll().stream().findFirst().orElse(new Empresa()));

        return "portal-tecnico"; 
    }

    // ==========================================
    // 🚦 2. ROTA INTELIGENTE: DESCOBRE O ID DO TÉCNICO LOGADO
    // ==========================================
    @GetMapping("/tecnico/painel")
    public String painelAutomatico(java.security.Principal principal) {
        String loginDoUsuario = removerAcentos(principal.getName().toLowerCase()); 
        List<com.suaempresa.gestao.model.Tecnico> todosOsTecnicos = tecnicoRepository.findAll();
        
        com.suaempresa.gestao.model.Tecnico tecnicoLogado = todosOsTecnicos.stream()
            .filter(t -> {
                if (t.getNome() == null) return false;
                String nomeLimpo = removerAcentos(t.getNome().toLowerCase());
                return nomeLimpo.contains(loginDoUsuario);
            })
            .findFirst()
            .orElse(null);

        if (tecnicoLogado != null) {
            return "redirect:/ordens/tecnico/" + tecnicoLogado.getId();
        } else {
            return "redirect:/login?erro=tecnico-nao-encontrado";
        }
    }

    private String removerAcentos(String texto) {
        return java.text.Normalizer.normalize(texto, java.text.Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
    }

    // ==========================================
    // ✅ 3. O BOTÃO ANTIGO DO APP (MANTIDO PARA EVITAR QUEBRAS)
    // ==========================================
    @GetMapping("/concluir-app/{id}")
    public String concluirPeloApp(@PathVariable Long id) {
        OrdemServico os = osRepository.findById(id).orElse(null);
        if(os != null) {
            os.setStatus("CONCLUIDA");
            
            String identificador = "O.S. #" + os.getId();
            List<Lancamento> todosLancamentos = lancamentoRepo.findAll();
            boolean jaFoiCobrado = todosLancamentos.stream()
                    .anyMatch(l -> l.getDescricao() != null && l.getDescricao().contains(identificador));
            
            if (!jaFoiCobrado) {
                Lancamento novaReceita = new Lancamento();
                String nomeCliente = (os.getCliente() != null) ? os.getCliente().getNome() : "Cliente não informado";
                novaReceita.setDescricao("Receita " + identificador + " - " + nomeCliente);
                novaReceita.setValor(os.getValor());
                novaReceita.setTipo("RECEITA");
                novaReceita.setStatus("PENDENTE");
                novaReceita.setData(LocalDate.now());
                lancamentoRepo.save(novaReceita);
            }
            
            osRepository.save(os);
            return "redirect:/ordens/tecnico/" + os.getTecnico().getId();
        }
        return "redirect:/";
    }

    // ==========================================
    // 📸 4. A NOVA ROTA VIP DO APP (COM FOTO E RELATO)
    // ==========================================
    @PostMapping("/tecnico/concluir")
    public String concluirPeloAppForm(@RequestParam("id") Long id, 
                                      @RequestParam(value = "servicoRealizado", required = false) String servicoRealizado,
                                      @RequestParam(value = "arquivosFotos", required = false) MultipartFile[] arquivos) {
        
        OrdemServico os = osRepository.findById(id).orElse(null);
        if(os != null) {
            os.setStatus("CONCLUIDA");
            
            // Salva o texto que o técnico digitou no celular
            if(servicoRealizado != null && !servicoRealizado.isEmpty()) {
                os.setServicoRealizado(servicoRealizado);
            }
            
            try {
                // Salva a foto do serviço (Câmera do celular)
                if (arquivos != null && arquivos.length > 0) {
                    for (MultipartFile arquivo : arquivos) {
                        if (!arquivo.isEmpty()) {
                            FotosOS novaFoto = new FotosOS();
                            novaFoto.setDadosImagem(arquivo.getBytes());
                            novaFoto.setTipoArquivo(arquivo.getContentType());
                            novaFoto.setOrdemServico(os);
                            os.getFotos().add(novaFoto);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Erro ao anexar foto do técnico: " + e.getMessage());
            }
            
            // Gatilho Financeiro
            String identificador = "O.S. #" + os.getId();
            List<Lancamento> todosLancamentos = lancamentoRepo.findAll();
            boolean jaFoiCobrado = todosLancamentos.stream()
                    .anyMatch(l -> l.getDescricao() != null && l.getDescricao().contains(identificador));
            
            if (!jaFoiCobrado) {
                Lancamento novaReceita = new Lancamento();
                String nomeCliente = (os.getCliente() != null) ? os.getCliente().getNome() : "Cliente não informado";
                novaReceita.setDescricao("Receita " + identificador + " - " + nomeCliente);
                novaReceita.setValor(os.getValor());
                novaReceita.setTipo("RECEITA");
                novaReceita.setStatus("PENDENTE");
                novaReceita.setData(LocalDate.now());
                lancamentoRepo.save(novaReceita);
            }
            
            osRepository.save(os);
            
            // O TÉCNICO VOLTA PARA A TELA DELE!
            return "redirect:/ordens/tecnico/painel";
        }
        return "redirect:/";
    }
}