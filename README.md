# 📋 Projeto Avaliativo: Gerenciador de Tarefas (Backend)

✨ Equipe
* Raul silva araujo
* João pedro borges araujo

##

Este projeto consiste em uma aplicação backend simples para o gerenciamento de tarefas diárias, desenvolvida como parte de um projeto avaliativo de Laboratório de Desenvolvimento Multiplataforma. Ele permite realizar operações CRUD (Create, Read, Update, Delete) em tarefas, utilizando tecnologias modernas do ecossistema Java.

## ✨ Funcionalidades

A API oferece os seguintes recursos para gerenciamento de tarefas:

*   **Criar Tarefa (Create):** Adiciona uma nova tarefa ao sistema.
*   **Listar Tarefas (Read):** Recupera todas as tarefas cadastradas.
*   **Atualizar Tarefa (Update):** Modifica os detalhes de uma tarefa existente.
*   **Deletar Tarefa (Delete):** Remove uma tarefa do sistema.

### Entidade `Tarefa`

Cada tarefa possui os seguintes atributos:

*   `id`: Identificador único da tarefa (gerado automaticamente).
*   `nome`: Nome da tarefa (ex: "Comprar Leite").
*   `descricao`: Descrição detalhada da tarefa.
*   `status`: Status atual da tarefa (ex: "PENDENTE", "CONCLUIDA", "EM_ANDAMENTO").
*   `observacoes`: Notas ou comentários adicionais sobre a tarefa.
*   `dataCriacao`: Timestamp da criação da tarefa.
*   `dataAtualizacao`: Timestamp da última atualização da tarefa.

## 🚀 Tecnologias Utilizadas

O projeto foi desenvolvido utilizando as seguintes tecnologias e frameworks:

*   **Linguagem:** Java 17+
*   **Framework:** Spring Boot 2.x (com Spring Data JPA e Spring Web)
*   **Banco de Dados:**
    *   PostgreSQL (recomendado para desenvolvimento e produção)
    *   H2 Database (em memória, para testes rápidos e desenvolvimento inicial)
*   **Ferramenta de Build:** Apache Maven
*   **Testes:** JUnit 5, Mockito
*   **Documentação API:** Springfox (Swagger/OpenAPI)

## ⚙️ Como Configurar e Rodar o Projeto

Siga os passos abaixo para configurar e executar a aplicação em seu ambiente local.

### Pré-requisitos

Certifique-se de ter os seguintes softwares instalados:

*   **Java Development Kit (JDK):** Versão 17 ou superior.
*   **Apache Maven:** Versão 3.x ou superior.
*   **Git:** Para clonar o repositório.
*   **Docker Desktop (Opcional, mas recomendado para PostgreSQL):** Para executar o banco de dados PostgreSQL em um contêiner.

### 1. Clonar o Repositório

Abra seu terminal ou prompt de comando e execute:

```bash
git clone https://github.com/RaulSAraujo/ldm # Substitua pela URL do seu repositório
cd ldm # Entre na pasta do projeto
