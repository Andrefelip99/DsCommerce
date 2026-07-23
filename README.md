# 🛒 DSCommerce

## 📋 Sobre o Projeto

DSCommerce é uma API REST de e-commerce desenvolvida durante o curso Java Spring Professional da DevSuperior, ministrado pelo professor Nélio Alves.

O projeto foi criado com o objetivo de aplicar conceitos fundamentais e avançados do ecossistema Spring, incluindo persistência de dados, autenticação, autorização, arquitetura em camadas e desenvolvimento de APIs REST.

A aplicação simula uma plataforma de comércio eletrônico, permitindo o gerenciamento de usuários, produtos, categorias e pedidos.

---

## 🚀 Tecnologias Utilizadas

* Java 17
* Spring Boot
* Spring Data JPA
* Hibernate
* Spring Security
* OAuth2
* JWT
* PostgreSQL
* Maven
* Lombok
* Bean Validation

---

## 🏗️ Arquitetura

```text
Controller
    ↓
DTO
    ↓
Service
    ↓
Repository
    ↓
Entity
```

O projeto segue o padrão de arquitetura em camadas, amplamente utilizado em aplicações corporativas desenvolvidas com Spring Boot.

---

## 🔐 Segurança

O sistema implementa autenticação e autorização utilizando:

* Spring Security
* OAuth2
* JWT (JSON Web Token)

### Perfis de Acesso

#### Cliente

* Visualizar produtos
* Consultar categorias
* Realizar pedidos
* Consultar pedidos próprios

#### Administrador

* Cadastro de produtos
* Atualização de produtos
* Exclusão de produtos
* Gerenciamento de categorias
* Gerenciamento de pedidos
* Controle geral da aplicação

---

## 📦 Principais Entidades

### Usuário

Responsável pelo acesso ao sistema.

### Produto

Itens disponíveis para venda.

### Categoria

Organização dos produtos por grupos.

### Pedido

Controle das compras realizadas pelos clientes.

### Item do Pedido

Representação dos produtos associados a um pedido.

### Pagamento

Informações relacionadas ao processamento dos pedidos.

---

## 📚 Conceitos Aplicados

### Back-End

* API REST
* Programação Orientada a Objetos
* Arquitetura em Camadas
* Injeção de Dependência

### Persistência

* PostgreSQL
* JPA
* Hibernate
* Relacionamentos entre Entidades

### Segurança

* Spring Security
* OAuth2
* JWT Authentication
* Controle de Permissões

### Organização

* DTO Pattern
* Tratamento de Exceções
* Validação de Dados
* Separação de Responsabilidades

---

## 🧪 Ferramentas Utilizadas

* Insomnia
* Postman
* Spring Tool Suite (STS)
* IntelliJ IDEA
* PostgreSQL

---

## ▶️ Como Executar

### Pré-requisitos

* Java 17+
* Maven
* PostgreSQL

### Clonar o Projeto

```bash
git clone https://github.com/seu-usuario/dscommerce
```

### Executar

```bash
mvn spring-boot:run
```

---

## 🎯 Objetivo do Projeto

Aplicar os principais conceitos utilizados no desenvolvimento de APIs REST profissionais com Spring Boot, seguindo as boas práticas ensinadas durante o curso Java Spring Professional da DevSuperior.

O projeto serviu como base para aprofundar conhecimentos em autenticação, autorização, persistência de dados, modelagem de entidades e desenvolvimento de aplicações corporativas.

---

## 🙏 Créditos

Projeto desenvolvido durante o curso Java Spring Professional da DevSuperior, ministrado por Nélio Alves.

---

## 👨‍💻 Autor

André Felipe

Estudante de Análise e Desenvolvimento de Sistemas e desenvolvedor Back-End com foco em Java e Spring Boot.
