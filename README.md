# 🎯 OpportunityScout Portugal

**Monitoramento Automático de Oportunidades de Negócio**

Sistema de coleta e centralização automática de oportunidades de negócio em Portugal, desenvolvido para facilitar o acesso a editais, avisos e incentivos públicos e privados.

---

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Stack Tecnológica](#stack-tecnológica)
- [Funcionalidades](#funcionalidades)
- [Regras de Negócio](#regras-de-negócio)
- [Instalação e Execução](#instalação-e-execução)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Uso do Sistema](#uso-do-sistema)
- [Credenciais Padrão](#credenciais-padrão)
- [Proprietário](#proprietário)
- [Licença](#licença)

---

## 🚀 Sobre o Projeto

O **OpportunityScout Portugal** é uma plataforma web fullstack que automatiza a busca e organização de oportunidades de negócio disponíveis em portais públicos portugueses. O sistema realiza scraping periódico de sites como **Portugal 2030** e **Compete 2030**, centralizando todas as informações em uma interface moderna e intuitiva.

### Objetivo

Facilitar o acesso de empresas e empreendedores a oportunidades de financiamento, incentivos e avisos públicos, eliminando a necessidade de consultar múltiplos sites manualmente.

### Diferenciais

- ✅ **Coleta automática** a cada 12 horas
- ✅ **Interface dark mode** moderna e responsiva
- ✅ **Filtros avançados** por fonte, categoria e palavra-chave
- ✅ **Sistema de autenticação** com JWT
- ✅ **Gestão de usuários** com perfis ADMIN e USER
- ✅ **Expansível** para incluir novas fontes (Diário da República, etc.)

---

## 🛠️ Stack Tecnológica

### **Backend**

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| **Java** | 21 | Linguagem principal |
| **Quarkus** | 3.15.1 | Framework supersônico para microserviços |
| **Hibernate Panache** | - | ORM simplificado para acesso a dados |
| **PostgreSQL** | 16 | Banco de dados relacional |
| **Jsoup** | 1.17.2 | Biblioteca para web scraping |
| **SmallRye JWT** | - | Autenticação e autorização JWT |
| **Quarkus Scheduler** | - | Agendamento de tarefas automáticas |
| **BCrypt** | - | Hash seguro de senhas |

### **Frontend**

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| **Next.js** | 14.2.5 | Framework React com SSR |
| **React** | 18.3.1 | Biblioteca para interfaces de usuário |
| **TypeScript** | 5.5.3 | Superset tipado de JavaScript |
| **Tailwind CSS** | 3.4.6 | Framework CSS utilitário |
| **Axios** | 1.7.2 | Cliente HTTP para requisições |
| **Lucide React** | 0.263.1 | Biblioteca de ícones |
| **date-fns** | 3.6.0 | Manipulação e formatação de datas |

### **Infraestrutura**

| Tecnologia | Descrição |
|------------|-----------|
| **Docker** | Containerização da aplicação |
| **Docker Compose** | Orquestração de múltiplos containers |
| **Maven** | Gerenciamento de dependências Java |
| **npm** | Gerenciamento de dependências Node.js |

---

## ⚙️ Funcionalidades

### 🔍 **Coleta Automática (Scraper)**

- Executa a cada **12 horas** via Quarkus Scheduler
- Coleta dados de:
  - **Compete 2030** (avisos de financiamento e incentivos)
  - **Portugal 2030** (programas estruturantes)
- Extrai informações:
  - Título da oportunidade
  - Código do aviso
  - Entidade responsável
  - Categoria
  - Datas de início/fim
  - Tipo de apoio (fundo perdido, cofinanciamento, etc.)
  - Beneficiários
  - Descrição
  - Link para a fonte original

### 🔐 **Autenticação e Autorização**

- Sistema de login com **JWT**
- Dois perfis de usuário:
  - **ADMIN**: Acesso total (gerenciar usuários + coletar oportunidades manualmente)
  - **USER**: Visualização de oportunidades

### 👥 **Gestão de Usuários (ADMIN)**

- Criar novos usuários
- Editar informações de usuários existentes
- Excluir usuários
- Alterar perfil (ADMIN/USER)
- Redefinir senhas

### 📊 **Visualização de Oportunidades**

- Listagem completa de oportunidades coletadas
- **Filtros**:
  - Por fonte (Portugal 2030, Compete 2030)
  - Por palavra-chave no título
  - Por categoria
- **Ordenação**: Mais recentes primeiro
- **Detalhamento expandível**: Clique em "Mais Info" para ver descrição completa e beneficiários
- **Link direto** para a fonte original

### 🔄 **Coleta Manual (ADMIN)**

- Botão "Coletar Agora" na página de oportunidades
- Permite forçar coleta imediata sem aguardar o agendamento

---

## 📐 Regras de Negócio

### **Autenticação**

1. Usuários devem estar autenticados para acessar o sistema
2. Token JWT tem validade de **24 horas**
3. Após expiração, o usuário é redirecionado para o login
4. Senhas são armazenadas com hash BCrypt

### **Perfis de Acesso**

| Permissão | ADMIN | USER |
|-----------|-------|------|
| Visualizar oportunidades | ✅ | ✅ |
| Filtrar e buscar | ✅ | ✅ |
| Coletar manualmente | ✅ | ❌ |
| Criar usuários | ✅ | ❌ |
| Editar usuários | ✅ | ❌ |
| Excluir usuários | ✅ | ❌ |

### **Coleta de Dados**

1. **Duplicação**: Oportunidades já existentes (mesmo link) não são inseridas novamente
2. **Validação**: Apenas oportunidades com título válido (>15 caracteres) são salvas
3. **Erro silencioso**: Falhas na coleta são logadas, mas não interrompem o sistema
4. **Timeout**: Requisições HTTP têm timeout de 20 segundos

### **Dados das Oportunidades**

- **Campos obrigatórios**: título, fonte, entidade, link
- **Campos opcionais**: código, categoria, datas, descrição, tipo de apoio, beneficiários
- **Metadados**: data de criação e última atualização automáticas

---

## 🚀 Instalação e Execução

### **Pré-requisitos**

- [Docker](https://www.docker.com/) (versão 20+)
- [Docker Compose](https://docs.docker.com/compose/) (versão 2+)

### **Passo a Passo**

1. **Clone o repositório**:
```bash
git clone <url-do-repositorio>
cd opportunityscout-portugal
```

2. **Gere as chaves JWT** (necessário apenas na primeira vez):
```bash
cd backend
chmod +x generate-keys.sh
./generate-keys.sh
cd ..
```

3. **Suba os containers**:
```bash
docker-compose up --build
```

4. **Acesse a aplicação**:
- **Frontend**: [http://localhost:3000](http://localhost:3000)
- **Backend API**: [http://localhost:8080](http://localhost:8080)
- **Banco de dados**: `localhost:5432`

### **Parar a aplicação**

```bash
docker-compose down
```

### **Limpar volumes e recomeçar do zero**

```bash
docker-compose down -v
docker-compose up --build
```

---

## 📁 Estrutura do Projeto

```
opportunityscout-portugal/
├── backend/                          # Backend Quarkus
│   ├── src/
│   │   └── main/
│   │       ├── java/com/opportunityscout/
│   │       │   ├── config/           # Configurações (Startup)
│   │       │   ├── dto/              # Data Transfer Objects
│   │       │   ├── entity/           # Entidades JPA (Usuario, Oportunidade)
│   │       │   ├── repository/       # Repositórios Panache
│   │       │   ├── resource/         # Controllers REST (JAX-RS)
│   │       │   ├── security/         # JWT Utils
│   │       │   └── service/          # Lógica de negócio e Scraper
│   │       └── resources/
│   │           ├── application.properties
│   │           └── META-INF/resources/  # Chaves JWT
│   ├── Dockerfile
│   ├── pom.xml
│   └── generate-keys.sh
│
├── frontend/                         # Frontend Next.js
│   ├── app/
│   │   ├── admin/usuarios/           # Página de gestão de usuários
│   │   ├── login/                    # Página de login
│   │   ├── oportunidades/            # Página principal
│   │   ├── globals.css
│   │   ├── layout.tsx
│   │   └── page.tsx
│   ├── components/
│   │   ├── ui/                       # Componentes reutilizáveis (Button, Input, Card)
│   │   └── Navbar.tsx
│   ├── contexts/
│   │   └── AuthContext.tsx           # Contexto de autenticação
│   ├── lib/
│   │   └── api.ts                    # Cliente HTTP Axios
│   ├── Dockerfile
│   ├── package.json
│   ├── tailwind.config.ts
│   └── tsconfig.json
│
└── docker-compose.yml                # Orquestração dos serviços
```

---

## 📖 Uso do Sistema

### **1. Login**

Acesse [http://localhost:3000](http://localhost:3000) e faça login com as credenciais padrão.

### **2. Visualizar Oportunidades**

Na página principal, você verá todas as oportunidades coletadas. Use os filtros para refinar sua busca:
- **Busca por palavra-chave**: Digite no campo de busca e pressione Enter ou clique em "Buscar"
- **Filtro por fonte**: Selecione "Portugal 2030" ou "Compete 2030" no dropdown
- **Detalhes expandidos**: Clique em "Mais Info" para ver descrição completa

### **3. Coletar Manualmente (ADMIN)**

Clique no botão **"Coletar Agora"** no topo da página. Uma mensagem exibirá quantas oportunidades foram encontradas.

### **4. Gerenciar Usuários (ADMIN)**

1. Clique em **"Usuários"** na navbar
2. Para criar: clique em **"Novo Usuário"** e preencha o formulário
3. Para editar: clique no ícone de lápis
4. Para excluir: clique no ícone de lixeira

### **5. Toggle de Senha**

Ao criar ou editar usuários, clique no ícone de olho para visualizar a senha digitada.

---

## 🏢 Proprietário

Este projeto é propriedade de:

**BARBAN SOFTWARES LTDA**

Todos os direitos reservados © 2025

---

## 📄 Licença

Este projeto é proprietário e de uso exclusivo da **BARBAN SOFTWARES LTDA**.

A redistribuição, modificação ou uso comercial sem autorização expressa é proibido.

---

## 🤝 Contribuições

Para sugestões ou melhorias, entre em contato com a **BARBAN SOFTWARES LTDA**.

---

## 📞 Suporte

Para dúvidas ou suporte técnico, contate:

**softwaresbarban@gmail.com**

---

**Desenvolvido com ❤️ por BARBAN SOFTWARES LTDA**