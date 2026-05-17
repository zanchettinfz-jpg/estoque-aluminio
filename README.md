# 🏭 Sistema de Controle de Estoque para Alumínio e Esquadrias

Sistema profissional desenvolvido em Java para gerenciamento completo de estoque de perfis de alumínio, esquadrias, sobras e movimentações industriais.

---

# 📌 Sobre o Projeto

Este sistema foi desenvolvido com foco em:

- controle profissional de estoque
- rastreabilidade de movimentações
- controle de sobras de perfis
- gerenciamento de usuários
- segurança e auditoria
- organização industrial

O objetivo é criar um ERP especializado para empresas do ramo de alumínio e esquadrias.

---

# 🚀 Tecnologias Utilizadas

## Backend
- Java 21

## Interface
- JavaFX

## Banco de Dados
- MySQL

## Gerenciador de Dependências
- Maven

## Arquitetura
- MVC
- Repository Pattern
- Service Layer

---

# 🎨 Interface Moderna

O sistema possui:
- tema escuro profissional
- interface moderna
- menu lateral
- dashboard
- tabelas organizadas
- tela de login segura
- design industrial

---

# 🔐 Segurança

O sistema possui:
- login de usuários
- aprovação manual de contas
- níveis de acesso
- rastreabilidade completa
- controle administrativo
- hash de senha com BCrypt
- auditoria de movimentações

---

# 👥 Níveis de Usuário

## 👑 Administrador
Possui acesso completo:
- gerenciamento de usuários
- aprovação de contas
- relatórios
- backup
- configurações
- auditoria completa

---

## 📦 Operador
Pode:
- entrada de estoque
- saída de estoque
- movimentações
- consultas

---

## 👁 Consulta
Pode apenas:
- visualizar estoque

---

# 📦 Funcionalidades

## ✅ Cadastro de Produtos
- código
- descrição
- linha
- cor
- tamanho
- unidade
- localização
- observações

---

## ✅ Controle de Estoque
- entrada
- saída
- devolução
- descarte
- perdas

---

## ✅ Rastreamento Completo
Toda movimentação registra:
- usuário responsável
- data e hora
- produto
- quantidade
- destino
- observação

---

## ✅ Controle de Sobras
- cadastro de sobras
- reaproveitamento
- localização
- controle de perdas

---

## ✅ Endereçamento de Estoque
Exemplo:
- A1
- A2
- B1
- B2

Estrutura preparada para:
- corredor
- estante
- posição

---

## ✅ Dashboard
- movimentações recentes
- estoque crítico
- produtos mais utilizados
- indicadores

---

# 🗄 Estrutura do Projeto

```bash
estoque-aluminio/
│
├── src/main/java/
│
├── controller/
├── service/
├── repository/
├── model/
├── util/
├── config/
├── view/
│
├── src/main/resources/
│   ├── css/
│   ├── images/
│   └── fxml/
│
├── pom.xml
└── README.md
```

---

# 🛠 Banco de Dados

O sistema utiliza:
- MySQL
- JDBC
- PreparedStatement
- Foreign Keys
- Relacionamentos

Tabelas principais:
- produtos
- usuarios
- movimentacoes
- sobras

---

# 📋 Requisitos

## Necessário instalar:
- Java JDK 21+
- MySQL Server
- Maven
- VS Code

---

# ▶ Como Executar

## 1. Clone o projeto

```bash
git clone https://github.com/seuusuario/estoque-aluminio.git
```

---

## 2. Entre na pasta

```bash
cd estoque-aluminio
```

---

## 3. Configure o MySQL

Crie o banco:

```sql
CREATE DATABASE estoque_aluminio;
```

---

## 4. Configure o arquivo de conexão

Exemplo:

```java
private static final String URL =
"jdbc:mysql://localhost:3306/estoque_aluminio";

private static final String USER = "root";

private static final String PASSWORD = "sua_senha";
```

---

## 5. Execute o projeto

```bash
mvn clean javafx:run
```

---

# 📌 Funcionalidades Futuras

- QR Code
- geração de PDF
- backup automático
- sistema em rede
- app mobile
- scanner de código
- integração com produção
- corte otimizado
- gráficos avançados
- integração com compras
  
---

# 🎯 Objetivo Final

Transformar o projeto em um sistema ERP profissional especializado no setor de alumínio e esquadrias.

---

# 📷 Screenshots

## Tela de Login
- login seguro
- design moderno
- aprovação de usuários

## Dashboard
- indicadores
- movimentações
- alertas

## Estoque
- consulta rápida
- rastreabilidade
- localização física

---

# 📄 Licença

Projeto privado para estudo, desenvolvimento e futura expansão comercial.

---

# 👨‍💻 Desenvolvedor

Desenvolvido por Nicolas.

Sistema focado em:
- organização industrial
- controle de estoque
- esquadrias
- alumínio
- rastreabilidade empresarial
