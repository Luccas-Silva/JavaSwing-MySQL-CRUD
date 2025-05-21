# Projeto Cadastro com JavaSwing MySQL

Este documento visa fornecer uma visão geral do projeto e suas funcionalidades. Para detalhes de implementação, consulte o código fonte.

## 1. Objetivo do Projeto

O principal objetivo deste projeto é criar uma aplicação de interface gráfica de usuário (GUI) simples para gerenciar o cadastro de clientes. A aplicação implementa as operações básicas de CRUD (Criar, Ler/Buscar, Atualizar e Deletar) e armazena as informações em um banco de dados MySQL.

## 2. Tecnologias Utilizadas

* **Linguagem de Programação:** Java
* **Interface Gráfica:** Swing (Java Swing Components)
* **Banco de Dados:** MySQL
* **IDE de Desenvolvimento:** IntelliJ IDEA 

## 3. Funcionalidades Implementadas

A aplicação de Cadastro de Clientes implementa as seguintes funcionalidades básicas de CRUD:

* **Inserir (Criar):** Permite cadastrar novos clientes no sistema, armazenando seus dados no banco de dados MySQL. Os campos obrigatórios são Nome e CPF.
* **Buscar (Ler):** Permite buscar um cliente existente no banco de dados através do seu CPF. Os dados do cliente encontrado são exibidos na interface.
* **Alterar (Atualizar):** Permite modificar os dados de um cliente existente no banco de dados, identificado pelo CPF.
* **Deletar:** Permite remover um cliente do banco de dados, identificado pelo CPF.
* **Listar Todos:** Exibe uma lista de todos os clientes cadastrados no banco de dados.

As seguintes funcionalidades extras foram implementadas neste projeto:

* **Máscara para Inputs:**
    * O campo CPF possui uma máscara de formatação para facilitar a entrada (`###.###.###-##`).
    * O campo Telefone possui uma máscara de formatação (`(##) #####-####`).
* **Validação para Inputs:**
    * **Nome:** Verifica se o campo não está vazio.
    * **CPF:** Verifica se o formato (após remoção da máscara) possui 11 dígitos.
    * **Telefone:** Verifica se, quando preenchido, possui um comprimento mínimo de 10 dígitos após remoção da máscara.
    * **Email:** Utiliza uma expressão regular para validar o formato do endereço de e-mail.
* **Solicitação de Confirmação:**
    * As ações de Inserir, Alterar e Deletar exibem uma caixa de diálogo de confirmação antes de serem executadas.


## 6. Instruções de Execução

Para executar este projeto, siga os seguintes passos:

1.  **Pré-requisitos:**
    * Java Development Kit (JDK) instalado no seu sistema.
    * IntelliJ IDEA instalado (ou outra IDE Java compatível).
    * Servidor MySQL em execução.
    * O driver MySQL Connector/J deve estar adicionado ao projeto como uma dependência.
    * Um banco de dados MySQL chamado `cadastro_clientes` deve ter sido criado, contendo uma tabela chamada `cliente` com a seguinte estrutura:
        ```sql
        CREATE TABLE cliente (
            id INT AUTO_INCREMENT PRIMARY KEY,
            nome VARCHAR(255) NOT NULL,
            cpf VARCHAR(14) UNIQUE NOT NULL,
            telefone VARCHAR(15),
            email VARCHAR(255) NOT NULL
        );
        ```

2.  **Configuração do Projeto na IDE:**
    * Abra o projeto no IntelliJ IDEA.
    * Verifique se o SDK do projeto está configurado corretamente para a sua instalação do JDK.
    * Certifique-se de que a biblioteca do MySQL Connector/J está listada nas dependências do projeto.

3.  **Configuração da Conexão com o Banco de Dados:**
    * O arquivo de configuração da conexão com o banco de dados (geralmente na classe `util.ConexaoMySQL`) deve conter as informações corretas para acessar o seu servidor MySQL (URL, nome de usuário, senha).

4.  **Execução da Aplicação:**
    * Localize a classe principal que contém o método `main` (geralmente a classe `ClienteForm` dentro do pacote `view`).
    * Execute esta classe a partir da IDE. A interface gráfica da aplicação deverá ser exibida.


