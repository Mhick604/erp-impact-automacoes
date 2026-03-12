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
    
    // Injeção do repositório financeiro para o gatilho funcionar
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
            // 1. Verifica se vieram arquivos da tela
            if (arquivos != null && arquivos.length > 0) {
                
                // 2. Passa por cada foto que o usuário selecionou
                for (MultipartFile arquivo : arquivos) {
                    
                    // 3. Se o arquivo não estiver vazio
                    if (!arquivo.isEmpty()) {
                        FotosOS novaFoto = new FotosOS();
                        novaFoto.setDadosImagem(arquivo.getBytes()); // Transforma a imagem em código blindado
                        novaFoto.setTipoArquivo(arquivo.getContentType()); // Salva se é PNG, JPG, etc.
                        novaFoto.setOrdemServico(ordemServico); // Diz de qual O.S. é essa foto
                        
                        // 4. Adiciona a foto na lista de fotos da O.S.
                        ordemServico.getFotos().add(novaFoto);
                    }
                }
            }
            
            // 5. Salva a Ordem de Serviço (E o Hibernate salva as fotos junto magicamente!)
            osRepository.save(ordemServico);
            
            // ==========================================
            // 🔥 6. O GATILHO FINANCEIRO AUTOMÁTICO 🔥
            // ==========================================
            if ("CONCLUIDA".equals(ordemServico.getStatus())) {
                
                // Verifica se já existe uma cobrança para essa O.S. para não duplicar
                String identificador = "O.S. #" + ordemServico.getId();
                
                List<Lancamento> todosLancamentos = lancamentoRepo.findAll();
                boolean jaFoiCobrado = todosLancamentos.stream()
                        .anyMatch(l -> l.getDescricao() != null && l.getDescricao().contains(identificador));
                
                // Se a O.S. foi concluída e AINDA NÃO FOI COBRADA, cria o lançamento!
                if (!jaFoiCobrado) {
                    Lancamento novaReceita = new Lancamento();
                    
                    // Tratamento caso o cliente venha vazio na OS
                    String nomeCliente = (ordemServico.getCliente() != null) ? ordemServico.getCliente().getNome() : "Cliente não informado";
                    
                    novaReceita.setDescricao("Receita " + identificador + " - " + nomeCliente);
                    novaReceita.setValor(ordemServico.getValor());
                    novaReceita.setTipo("RECEITA");
                    novaReceita.setStatus("PENDENTE"); // Cai no contas a receber!
                    novaReceita.setData(LocalDate.now());
                    
                    // Salva no cofre do Financeiro!
                    lancamentoRepo.save(novaReceita);
                    
                    System.out.println("💰 Gatilho Disparado! Nova cobrança gerada para a " + identificador);
                }
            }
            
        } catch (Exception e) {
            System.out.println("Erro ao salvar a OS/imagem: " + e.getMessage());
        }
        
        // 7. Volta para a lista de Ordens de Serviço
        return "redirect:/ordens";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        osRepository.deleteById(id);
        return "redirect:/ordens";
    }
    
    // Rota para gerar a O.S. em tela cheia para PDF
    @GetMapping("/imprimir/{id}")
    public String imprimirOS(@PathVariable Long id, Model model) {
        // Busca a O.S. específica
        OrdemServico os = osRepository.findById(id).orElse(null);
        if(os == null) {
            return "redirect:/ordens"; // Se não achar, volta pra lista
        }
        
        // Busca os dados da Impact Automações para o cabeçalho
        Empresa empresa = empresaRepository.findAll().stream().findFirst().orElse(new Empresa());
        
        model.addAttribute("os", os);
        model.addAttribute("empresa", empresa);
        
        return "os-imprimir"; // Vai abrir a tela limpa
    }
    
    // --- O CANO DE IMAGENS (Busca a foto no Banco e joga pra tela) ---
    @GetMapping("/foto/{idOs}/{indice}")
    public ResponseEntity<byte[]> exibirFoto(@PathVariable Long idOs, @PathVariable int indice) {
        
        Optional<OrdemServico> osOptional = osRepository.findById(idOs);
        
        if (osOptional.isPresent()) {
            OrdemServico os = osOptional.get();
            
            // Verifica se a O.S. tem fotos e se o índice procurado existe
            if (os.getFotos() != null && os.getFotos().size() > indice) {
                FotosOS foto = os.getFotos().get(indice);
                
                // Pega o código da imagem e transforma na foto real para o navegador
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, foto.getTipoArquivo())
                        .body(foto.getDadosImagem());
            }
        }
        
        // Se der errado, retorna erro 404 (Não Encontrado)
        return ResponseEntity.notFound().build();
    }
    
 // ==========================================
    // 📱 ROTAS DO APP DO TÉCNICO (CELULAR) 📱
    // ==========================================
    
    // 1. Abre a tela do celular filtrando só as O.S. do técnico
    @GetMapping("/tecnico/{tecnicoId}")
    public String portalTecnico(@PathVariable Long tecnicoId, Model model) {
        // Busca o técnico no banco de dados para dar "Boas vindas"
        com.suaempresa.gestao.model.Tecnico tecnico = tecnicoRepository.findById(tecnicoId).orElse(null);
        
        // Puxa as O.S. e filtra: Só do técnico selecionado E que não estejam concluídas
        List<OrdemServico> osDoTecnico = osRepository.findAll().stream()
            .filter(os -> os.getTecnico() != null && os.getTecnico().getId().equals(tecnicoId))
            .filter(os -> !"CONCLUIDA".equals(os.getStatus()))
            .toList();

        model.addAttribute("ordens", osDoTecnico);
        model.addAttribute("tecnico", tecnico);

        return "portal-tecnico"; // Vai chamar nosso novo HTML
    }

    // 2. O botão verde gigante do App (Conclui e dispara o financeiro)
    @GetMapping("/concluir-app/{id}")
    public String concluirPeloApp(@PathVariable Long id) {
        OrdemServico os = osRepository.findById(id).orElse(null);
        if(os != null) {
            os.setStatus("CONCLUIDA");
            
            // Repete o gatilho financeiro!
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
            
            // Devolve o técnico para a tela do App dele
            return "redirect:/ordens/tecnico/" + os.getTecnico().getId();
        }
        return "redirect:/";
    }
}