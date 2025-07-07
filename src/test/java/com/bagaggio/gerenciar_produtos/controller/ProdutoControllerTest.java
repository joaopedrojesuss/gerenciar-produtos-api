package com.bagaggio.gerenciar_produtos.controller;


import com.bagaggio.gerenciar_produtos.dto.ProdutoDTO;
import com.bagaggio.gerenciar_produtos.model.Produto;
import com.bagaggio.gerenciar_produtos.repository.ProdutoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração para o {@link ProdutoController}.
 * Verifica o comportamento da API REST para operações CRUD de produtos.
 * Utiliza um banco de dados H2 em memória configurado no perfil "test".
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Configuração inicial antes de cada teste.
     * Limpa o banco de dados para garantir isolamento entre os testes.
     */
    @BeforeEach
    void setUp() {
        produtoRepository.deleteAll();
    }

    /**
     * Testa o cenário onde não há produtos cadastrados.
     * Deve retornar uma lista vazia com status HTTP 200.
     */
    @Test
    void listarTodos_DeveRetornarListaVaziaQuandoNaoTiverProdutos() throws Exception {
        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    /**
     * Testa o retorno de todos os produtos quando existem registros.
     * Verifica se a lista contém os produtos cadastrados com seus respectivos dados.
     */
    @Test
    void listarTodos_DeveRetornarProdutosQuandoExistirem() throws Exception {
        // Cria e salva dois produtos no banco de dados
        Produto produto1 = new Produto(null, "Produto 1", "Descrição 1", BigDecimal.valueOf(10.99), 5, "Categoria 1");
        Produto produto2 = new Produto(null, "Produto 2", "Descrição 2", BigDecimal.valueOf(20.50), 10, "Categoria 2");
        produtoRepository.saveAll(List.of(produto1, produto2));

        // Verifica a resposta da API
        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nome", is("Produto 1")))
                .andExpect(jsonPath("$[1].nome", is("Produto 2")));
    }

    /**
     * Testa a busca de um produto por ID existente.
     * Deve retornar o produto com status HTTP 200.
     */
    @Test
    void buscarPorId_DeveRetornarProdutoQuandoExistir() throws Exception {
        // Salva um produto no banco de dados
        Produto produto = produtoRepository.save(
                new Produto(null, "Produto Teste", "Descrição Teste", BigDecimal.valueOf(15.99), 8, "Categoria Teste"));

        mockMvc.perform(get("/produtos/{id}", produto.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(produto.getId().intValue())))
                .andExpect(jsonPath("$.nome", is("Produto Teste")));
    }

    /**
     * Testa a busca por um ID que não existe.
     * Deve retornar status HTTP 404 com mensagem de erro.
     */
    @Test
    void buscarPorId_DeveRetornarNotFoundQuandoNaoExistir() throws Exception {
        mockMvc.perform(get("/produtos/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("Produto não encontrado")));
    }

    /**
     * Testa a criação de um novo produto com dados válidos.
     * Deve retornar o produto criado com status HTTP 201.
     */
    @Test
    void criar_DeveSalvarProdutoQuandoDadosForemValidos() throws Exception {
        // Cria um DTO válido
        ProdutoDTO produtoDTO = new ProdutoDTO(null, "Novo Produto", "Nova Descrição",
                BigDecimal.valueOf(25.99), 15, "Nova Categoria");

        // Verifica a resposta da API
        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.nome", is("Novo Produto")));

        List<Produto> produtos = produtoRepository.findAll();
        assertEquals(1, produtos.size());
        assertEquals("Novo Produto", produtos.getFirst().getNome());
    }

    /**
     * Testa a criação de produto com dados inválidos.
     * Deve retornar status HTTP 400 com detalhes dos erros de validação.
     */
    @Test
    void criar_DeveRetornarBadRequestQuandoDadosForemInvalidos() throws Exception {
        // Cria um DTO com todos os campos inválidos
        ProdutoDTO produtoDTO = new ProdutoDTO(null, "", "", BigDecimal.valueOf(-5), -1, "");

        // Verifica a resposta do erro
        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nome", notNullValue()))
                .andExpect(jsonPath("$.descricao", notNullValue()))
                .andExpect(jsonPath("$.preco", notNullValue()))
                .andExpect(jsonPath("$.quantidade", notNullValue()))
                .andExpect(jsonPath("$.categoria", notNullValue()));
    }

    /**
     * Testa a atualização de um produto existente.
     * Deve retornar o produto atualizado com status HTTP 200.
     */
    @Test
    void atualizar_DeveAtualizarProdutoQuandoExistir() throws Exception {
        // Cria e salva um produto no banco
        Produto produtoExistente = produtoRepository.save(
                new Produto(null, "Produto Antigo", "Descrição Antiga", BigDecimal.valueOf(10.0), 5, "Categoria Antiga"));

        // DTO com dados atualizados
        ProdutoDTO produtoDTO = new ProdutoDTO(null, "Produto Atualizado", "Descrição Atualizada",
                BigDecimal.valueOf(20.0), 10, "Categoria Atualizada");

        // Verifica a resposta da API
        mockMvc.perform(put("/produtos/{id}", produtoExistente.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Produto Atualizado")))
                .andExpect(jsonPath("$.descricao", is("Descrição Atualizada")))
                .andExpect(jsonPath("$.preco", is(20.0)))
                .andExpect(jsonPath("$.quantidade", is(10)))
                .andExpect(jsonPath("$.categoria", is("Categoria Atualizada")));
    }

    /**
     * Testa a atualização de um produto que não existe.
     * Deve retornar status HTTP 404 com mensagem de erro.
     */
    @Test
    void atualizar_DeveRetornarNotFoundQuandoNaoExistir() throws Exception {
        // Cri um DTO válido
        ProdutoDTO produtoDTO = new ProdutoDTO(null, "Produto Inexistente", "Descrição",
                BigDecimal.valueOf(10.0), 5, "Categoria");

        // Tenta atualizar um ID inexistente
        mockMvc.perform(put("/produtos/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("Produto não encontrado")));
    }

    /**
     * Testa a exclusão de um produto existente.
     * Deve retornar status HTTP 204 (No Content).
     */
    @Test
    void deletar_DeveRemoverProdutoQuandoExistir() throws Exception {
        // Cria e salva um produto
        Produto produto = produtoRepository.save(
                new Produto(null, "Produto para Deletar", "Descrição", BigDecimal.valueOf(10.0), 5, "Categoria"));

        // Verifica a resposta e se o produto foi removido
        mockMvc.perform(delete("/produtos/{id}", produto.getId()))
                .andExpect(status().isNoContent());

        assertEquals(0, produtoRepository.count());
    }

    /**
     * Testa a exclusão de um produto que não existe.
     * Deve retornar status HTTP 404 com mensagem de erro.
     */
    @Test
    void deletar_DeveRetornarNotFoundQuandoNaoExistir() throws Exception {
        mockMvc.perform(delete("/produtos/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("Produto não encontrado")));
    }

    /**
     * Testa o tratamento de JSON mal formado na criação.
     * Deve retornar status HTTP 400.
     */
    @Test
    void criar_DeveRetornarBadRequestQuandoJsonForMalFormado() throws Exception {
        String jsonMalFormado = "{\"nome\": \"Produto\", \"preco\": \"texto_invalido\"}";

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMalFormado))
                .andExpect(status().isBadRequest());
    }

    /**
     * Testa se o ID é gerado automaticamente na criação.
     * Verifica se o ID retornado não é nulo e maior que zero.
     */
    @Test
    void criar_DeveVerificarSeIdEhGeradoAutomaticamente() throws Exception {
        ProdutoDTO produtoDTO = new ProdutoDTO(null, "Produto Teste", "Descrição Teste",
                BigDecimal.valueOf(15.99), 10, "Categoria Teste");

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.id", greaterThan(0)));
    }

    /**
     * Testa o tratamento de ID inválido na URL.
     * Deve retornar status HTTP 400.
     */
    @Test
    void buscarPorId_RetornarBadRequestQuandoIdForInvalido() throws Exception {
        mockMvc.perform(get("/produtos/{id}", "abc"))
                .andExpect(status().isBadRequest());
    }
}

