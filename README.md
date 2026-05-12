# Estoque Alumínio

Sistema desktop profissional para controle de estoque de perfis de alumínio, esquadrias e sobras, desenvolvido em JavaFX com Maven e SQLite.

## Recursos

- Login preparado para níveis de acesso
- Dashboard com indicadores, gráficos e últimas movimentações
- Cadastro, edição, exclusão e busca de produtos
- Movimentações de entrada e saída com atualização automática de estoque
- Histórico filtrável por produto, tipo e data
- Controle de sobras de barras
- Estrutura em MVC com Repository Pattern e Service Layer
- Preparado para QR Code, relatórios PDF, backup, PostgreSQL e Spring Boot

## Requisitos

- Java 17 ou superior
- Maven 3.9 ou superior
- VS Code com extensões Java recomendadas

## Executar

```bash
mvn javafx:run
```

Usuário inicial criado automaticamente:

- Login: `admin`
- Senha: `admin`

## Estrutura

O banco SQLite é criado automaticamente em `src/main/resources/database/estoque.db` na primeira execução.
