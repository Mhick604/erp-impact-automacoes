# 🚀 Impact ERP - Sistema de Gestão Operacional

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.3-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-%23005C0F.svg?style=for-the-badge&logo=Thymeleaf&logoColor=white)
![Bootstrap](https://img.shields.io/badge/Bootstrap_5-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white)

O **Impact ERP** é um sistema completo de gestão desenhado para empresas de prestação de serviços, manutenção e automação. Ele conecta a administração central (Dashboard Administrativo) aos técnicos que estão na rua (Portal Mobile do Técnico), amarrando estoque, financeiro e ordens de serviço em um fluxo de dados inteligente.

## ✨ Funcionalidades Principais

* 📊 **Dashboard Gerencial:** Visão em tempo real do caixa, O.S. pendentes de faturamento e "radar" de técnicos em campo.
* 📋 **Gestão de Ordens de Serviço (O.S.):** Criação de orçamentos e O.S. definitivas. Suporte para upload de fotos, anexação de nota fiscal (PDF) e geração de versão para impressão.
* 📱 **Portal do Técnico (Web App):** Visão exclusiva e responsiva para o funcionário de campo acessar sua pauta de serviços do dia, dar check-in, anexar fotos e descrever o laudo da visita.
* 📦 **Controle de Estoque e Arsenal:** Cadastro de materiais de consumo e ferramentas. **Gatilho Inteligente:** Ao adicionar um material na O.S., o sistema dá baixa automática na quantidade em estoque.
* 💰 **Módulo Financeiro:** Fluxo de caixa (Receitas e Despesas). **Gatilho Inteligente:** O.S. marcadas como "Concluídas" geram automaticamente uma "Receita Pendente" no financeiro.
* 💬 **Integração com WhatsApp:** Botões de atalho que geram mensagens parametrizadas e abrem o WhatsApp Web/App para enviar laudos, cobranças e ordens diretamente ao cliente ou técnico.
* 🎨 **White-Label e Configurações:** Customização da plataforma com upload de logotipo da empresa (armazenado em banco via Byte Array) e injeção dinâmica da cor principal da marca em toda a interface CSS.
* 🛡️ **Controle de Acessos (Spring Security):** Rotas protegidas e divisão hierárquica (`ROLE_ADMIN`, `ROLE_TECNICO`, `ROLE_BLOQUEADO`).

## 🛠️ Arquitetura e Tecnologias

**Backend:**
* **Java 17**
* **Spring Boot 3.4.3** (Web, Data JPA, Security, Validation)
* **Bancos de Dados:** H2 Database (Perfil de Desenvolvimento) e PostgreSQL (Perfil de Produção)
* **Maven** (Gerenciamento de dependências)

**Frontend:**
* **Thymeleaf** (Motor de templates server-side integrado ao Spring)
* **Bootstrap 5** & **Bootstrap Icons** (Design responsivo e componentes)
* **Vanilla JS** (Máscaras de formulários e manipulação de DOM)

**DevOps & Deploy:**
* **Docker** (Dockerfile multi-stage build incluído)
* **Spring Profiles** (`dev`, `prod`)

## ⚙️ Como Executar o Projeto Localmente

### Pré-requisitos
* [JDK 17+](https://adoptium.net/) instalado.
* Maven (Opcional, pois o projeto inclui o *Maven Wrapper*).

### Passo a Passo

1. **Clone o repositório:**
   ```bash
   git clone [https://github.com/SeuUsuario/erp-impact-automacoes.git](https://github.com/SeuUsuario/erp-impact-automacoes.git)
   cd erp-impact-automacoes
