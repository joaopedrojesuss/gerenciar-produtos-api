# 🛍️ Gerenciador de Produtos - API REST

API completa para gerenciamento de produtos com operações CRUD, desenvolvida em Spring Boot 3.5.3 e Java 21. 
Inclui validação de dados, documentação Swagger/OpenAPI, testes automatizados e arquitetura em camadas.

## 📋 Índice

- Características
- Tecnologias Utilizadas
- Pré-requisitos
- Instalação e Configuração
- Execução da Aplicação
- Documentação da API
- Endpoints
- Exemplos de Uso
- Testes
- Configuração do Banco de Dados
- Estrutura do Projeto
- Configurações Importantes
- Licença
- Autor
- Contato


## ✨ Características

- **API REST completa** com operações CRUD
- **Validação de dados** com Bean Validation
- **Documentação automática** com OpenAPI/Swagger
- **Testes unitários e de integração** com JUnit 5 e Mockito
- **Banco de dados MySQL** para produção
- **Banco H2 em memória** para testes
- **Tratamento global de exceções**
- **Arquitetura em camadas** (Controller, Service, Repository)
- **DTOs** para transferência de dados
- **Lombok** para redução de código boilerplate

## 🛠️ Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.5.3**
- **Spring Data JPA**
- **Spring Web**
- **Spring Boot Validation**
- **MySQL 8.0+**
- **H2 Database** (para testes)
- **Lombok**
- **OpenAPI/Swagger**
- **JUnit 5**
- **Mockito**
- **Maven**

## 📋 Pré-requisitos

Antes de executar o projeto, certifique-se de ter instalado:

- **Java 21** ou superior
- **Maven 3.6+**
- **MySQL 8.0+** (para ambiente de produção)
- **Git** (para clonar o repositório)

## 🚀 Instalação e Configuração

### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/gerenciar-produtos.git
cd gerenciar-produtos
```

### 2. Configure o banco de dados MySQL

Crie um banco de dados MySQL e configure as credenciais no arquivo `application.properties`:

```properties
# Configurações do MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/gerenciar_produtos?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

### 3. Instale as dependências

```bash
mvn clean install
```

## ▶️ Execução da Aplicação

### Executar com Maven

```bash
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

## 📚 Documentação da API

A documentação interativa da API está disponível através do Swagger UI:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/api-docs`

## 🔗 Endpoints

### Produtos

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/produtos` | Lista todos os produtos |
| `GET` | `/produtos/{id}` | Busca produto por ID |
| `POST` | `/produtos` | Cria novo produto |
| `PUT` | `/produtos/{id}` | Atualiza produto existente |
| `DELETE` | `/produtos/{id}` | Remove produto |

### Estrutura do Produto

```json
{
  "id": 1,
  "nome": "Mochila Executiva",
  "descricao": "Mochila para notebook com compartimentos organizadores",
  "preco": 199.99,
  "quantidade": 25,
  "categoria": "Mochilas"
}
```

## 📝 Exemplos de Uso

### Criar um novo produto

```bash
curl -X POST http://localhost:8080/produtos \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Mala de Viagem",
    "descricao": "Mala grande para viagens longas",
    "preco": 299.99,
    "quantidade": 10,
    "categoria": "Malas"
  }'
```

### Listar todos os produtos

```bash
curl -X GET http://localhost:8080/produtos
```

### Buscar produto por ID

```bash
curl -X GET http://localhost:8080/produtos/1
```

### Atualizar produto

```bash
curl -X PUT http://localhost:8080/produtos/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Mala de Viagem",
    "descricao": "Mala grande",
    "preco": 399.99,
    "quantidade": 5,
    "categoria": "Malas"
  }'
```

### Remover produto

```bash
curl -X DELETE http://localhost:8080/produtos/1
```

## 🧪 Testes

O projeto inclui testes unitários e de integração abrangentes.

### Executar todos os testes

```bash
mvn test
```

### Tipos de Testes

- **Testes Unitários**: Testam a lógica de negócio do `ProdutoService`
- **Testes de Integração**: Testam os endpoints da API com `MockMvc`
- **Banco H2**: Usado nos testes para isolamento

## 🗄️ Configuração do Banco de Dados

### Ambiente de Produção (MySQL)

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gerenciar_produtos?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=suasenha
spring.jpa.hibernate.ddl-auto=update
```

### Ambiente de Testes (H2)

```properties
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MYSQL
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=create-drop
spring.h2.console.enabled=true
```

## 📁 Estrutura do Projeto

```
src/
├── main/
│   ├── java/
│   │   └── com/bagaggio/gerenciar_produtos/
│   │       ├── config/           # Configurações (OpenAPI)
│   │       ├── controller/       # Controllers REST (ProdutoController, HomeController)
│   │       ├── dto/              # Data Transfer Objects (ProdutoDTO)
│   │       ├── exception/        # Tratamento de exceções (GlobalExceptionHandler)
│   │       ├── model/            # Entidades JPA (Produto)
│   │       ├── repository/       # Repositórios JPA (ProdutoRepository)
│   │       └── service/          # Lógica de negócio (ProdutoService)
│   └── resources/
│       ├── db/init/              # Scripts de inicialização (schema.sql)
│       └── application.properties # Configs do MySQL/Spring
└── test/
    ├── java/
    │   └── com/bagaggio/gerenciar_produtos/
    │       ├── controller/       # Testes de integração (ProdutoControllerTest.java)
    │       └── service/          # Testes unitários (ProdutoServiceTest.java)
    └── resources/
        └── application-test.properties # Configs do H2 (para testes) 
```

## 🔧 Configurações Importantes

### Validações do DTO

- **Nome**: Obrigatório, máximo 100 caracteres
- **Descrição**: Obrigatória
- **Preço**: Obrigatório, deve ser positivo, máximo 16 dígitos inteiros e 2 decimais
- **Quantidade**: Não pode ser negativa
- **Categoria**: Obrigatória, máximo 50 caracteres

### Tratamento de Erros

A API retorna respostas padronizadas para erros:

```json
{
  "status": 404,
  "message": "Produto não encontrado!",
  "timestamp": 1704067200000
}
```

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

## 👨‍💻 Autor

Desenvolvido por João Pedro de Jesus, para processo seletivo.

## 📞 Contato

- Email: joaopedrojesusilva@outlook.com
- LinkedIn: www.linkedin.com/in/joao-pedrojesus/
