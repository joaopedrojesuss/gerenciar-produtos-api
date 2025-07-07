package com.bagaggio.gerenciar_produtos.service;

import com.bagaggio.gerenciar_produtos.dto.ProdutoDTO;
import com.bagaggio.gerenciar_produtos.model.Produto;
import com.bagaggio.gerenciar_produtos.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para {@link ProdutoService}.
 * Verifica a lógica de negócios e conversões entre DTO e Entity.
 * Utiliza Mockito para isolar os testes do banco de dados real.
 */
@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    private Produto produto;
    private ProdutoDTO produtoDTO;

    /**
     * Configuração inicial para cada teste.
     * Cria instâncias de Produto e ProdutoDTO para reutilização nos testes.
     */
    @BeforeEach
    void setUp(){
        produto = new Produto(1L, "Produto Test", "Descrição Teste",
            BigDecimal.valueOf(25.99), 20, "Categoria Teste");
        produtoDTO = new ProdutoDTO(1L, "Produto Teste", "Descrição Teste",
            BigDecimal.valueOf(25.99), 20, "Categoria Teste");
    }

    /**
     * Testa a listagem de todos os produtos.
     * Verifica se:
     *  A lista retornada contém o número correto de itens
     *  Os dados do produto são convertidos corretamente para DTO
     *  O repositório foi chamado uma vez
     */
    @Test
    void listarTodos_DeveRetornarListaDeProdutosDTO() {
        // Configura o mock
        when(produtoRepository.findAll()).thenReturn(List.of(produto));

        // Executa o metodo
        List<ProdutoDTO> result = produtoService.listarTodos();

        // Verificações
        assertEquals(1, result.size(), "Deveria retornar uma lista com 1 produto");
        assertEquals(produtoDTO.getId(), result.get(0).getId(), "O ID do produto deveria corresponder");
        verify(produtoRepository, times(1)).findAll();
    }

    /**
     * Testa a busca por ID quando o produto existe.
     * Verifica se:
     *  O produto é encontrado
     *  A conversão para DTO mantém os dados corretos
     *  O repositório foi chamado com o ID correto
     */
    @Test
    void buscarPorId_DeveRetornarProdutoDTOQuandoExistir() {
        // Configura o mock
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        // Executa o metodo
        ProdutoDTO result = produtoService.buscarPorId(1L);

        // Verificações
        assertNotNull(result);
        assertEquals(produtoDTO.getId(), result.getId());
        verify(produtoRepository, times(1)).findById(1L);
    }

    /**
     * Testa a busca por ID quando o produto não existe.
     * Verifica se:
     *  Uma exceção é lançada
     *  O repositório foi chamado com o ID correto
     */
    @Test
    void buscarPorId_DeveLancarExcecaoQuandoNaoExistir() {
        // Configura o mock para retornar vazio
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        // Verifica se a exceção é lançada
        assertThrows(RuntimeException.class, () -> produtoService.buscarPorId(1L), "Deveria lança exceção quando o produto não existe");
        verify(produtoRepository, times(1)).findById(1L);
    }

    /**
     * Testa a criação de um novo produto.
     * Verifica se:
     *  O produto é salvo no repositório
     *  O DTO retornado contém os dados corretos
     *  O repositório foi chamado uma vez
     */
    @Test
    void criarProduto_DeveSalvarERetornarProdutoDTO() {
        // Configura o mock
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        // Executa o método
        ProdutoDTO result = produtoService.criarProduto(produtoDTO);

        // Verificações
        assertNotNull(result);
        assertEquals(produtoDTO.getId(), result.getId());
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    /**
     * Testa a atualização de um produto existente.
     * Verifica se:
     *  O produto é atualizado corretamente
     *  O DTO retornado contém os novos dados
     *  Os métodos do repositório são chamados corretamente
     */
    @Test
    void atualizarProduto_DeveAtualizarERetornarProdutoDTO_QuandoExistir() {
        // Configura os mocks
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        // Executa o metodo
        ProdutoDTO result = produtoService.atualizarProduto(1L, produtoDTO);

        // Verificações
        assertNotNull(result);
        assertEquals(produtoDTO.getId(), result.getId());
        verify(produtoRepository, times(1)).findById(1L);
        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    /**
     * Testa a atualização de um produto que não existe.
     * Verifica se:
     *  Uma exceção é lançada
     *  O metodo save não é chamado
     */
    @Test
    void atualizarProduto_DeveLancarExcecaoQuandoNaoExistir() {
        // Configura o mock para retornar vazio
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        // Verifica se a exceção é lançada
        assertThrows(RuntimeException.class, () -> produtoService.atualizarProduto(1L, produtoDTO), "Deveria lançar exceção quando o produto não existe");
        verify(produtoRepository, times(1)).findById(1L);
        verify(produtoRepository, never()).save(any(Produto.class));
    }

    /**
     * Testa a exclusão de um produto existente.
     * Verifica se:
     *  Retorna true quando o produto existe
     *  O metodo delete é chamado
     */
    @Test
    void deletarProduto_DeveRetornarTrueQuandoExistir() {
        // Configura o mock
        when(produtoRepository.existsById(1L)).thenReturn(true);

        // Executa o metodo
        boolean result = produtoService.deletarProduto(1L);

        // Verificações
        assertTrue(result);
        verify(produtoRepository, times(1)).existsById(1L);
        verify(produtoRepository, times(1)).deleteById(1L);
    }

    /**
     * Testa a exclusão de um produto que não existe.
     * Verifica se:
     *  Retorna false quando o produto não existe
     *  O metodo delete não é chamado
     */
    @Test
    void deletarProduto_DeveRetornarFalseQuandoNaoExistir() {
        // Configura o mock
        when(produtoRepository.existsById(1L)).thenReturn(false);

        // Executa o método
        boolean result = produtoService.deletarProduto(1L);

        // Verificações
        assertFalse(result);
        verify(produtoRepository, times(1)).existsById(1L);
        verify(produtoRepository, never()).deleteById(1L);
    }

    /**
     * Testa a conversão de DTO para Entity.
     * Verifica se todos os campos são convertidos corretamente.
     */
    @Test
    void criarProduto_DeveConverterDTOParaEntity() {
        // Cria um DTO com dados de teste
        ProdutoDTO dto = new ProdutoDTO(
                null,
                "Mochila Grande",
                "Descricao Test",
                BigDecimal.valueOf(369.99),
                5,
                "Mochilas"
        );

        // Configura o mock para simular a persistência
        when(produtoRepository.save(any(Produto.class))).thenAnswer(invocation -> {
            Produto p = invocation.getArgument(0);
            p.setId(1L); // Simula a geração do ID
            return p;
        });

        // Executa o metodo
        ProdutoDTO result = produtoService.criarProduto(dto);

        // Verificações
        assertNotNull(result.getId());
        assertEquals(dto.getNome(), result.getNome());
        assertEquals(dto.getDescricao(), result.getDescricao());
        assertEquals(dto.getPreco().setScale(2, RoundingMode.HALF_EVEN), result.getPreco());
        assertEquals(dto.getQuantidade(), result.getQuantidade());
        assertEquals(dto.getCategoria(), result.getCategoria());
    }

    /**
     * Testa a conversão de Entity para DTO.
     * Verifica se todos os campos são convertidos corretamente.
     */
    @Test
    void buscarPorId_DeveConverterEntityParaDTOCorretamente() {
        // Cria uma entidade de teste
        Produto produtoSalvo = new Produto(
                1L,
                "Mala Pequena",
                "Descricao Test2",
                BigDecimal.valueOf(102.99),
                10,
                "Malas"
        );

        // Configura o mock
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produtoSalvo));

        // Executa o método
        ProdutoDTO result = produtoService.buscarPorId(1L);

        // Verificações
        assertEquals(produtoSalvo.getId(), result.getId());
        assertEquals(produtoSalvo.getNome(), result.getNome());
        assertEquals(produtoSalvo.getDescricao(), result.getDescricao());
        assertEquals(produtoSalvo.getPreco(), result.getPreco());
        assertEquals(produtoSalvo.getQuantidade(), result.getQuantidade());
        assertEquals(produtoSalvo.getCategoria(), result.getCategoria());
    }

    /**
     * Testa o arredondamento do preço para 2 casas decimais.
     * Verifica se:
     *  O preço é arredondado corretamente antes de ser persistido
     *  O DTO retornado contém o valor arredondado
     */
    @Test
    void deveArredondarPrecoParaDuasCasasDecimais() {
        // Cria um DTO com preço que precisa ser arredondado
        ProdutoDTO dto = new ProdutoDTO(
                null,
                "Produto",
                "Descricao",
                BigDecimal.valueOf(329.987),
                1,
                "Bolsa"
        );

        // Configura o mock para verificar o arredondamento
        when(produtoRepository.save(any(Produto.class))).thenAnswer(invocation -> {
            Produto p = invocation.getArgument(0);
            p.setId(1L);
            assertEquals(BigDecimal.valueOf(329.99), p.getPreco()); // Verifica se o preço foi arredondado antes de salvar
            return p;
        });

        // Executa o método
        ProdutoDTO result = produtoService.criarProduto(dto);

        // Verificações finais
        assertEquals(0, BigDecimal.valueOf(329.99).compareTo(result.getPreco()), "O preço no DTO deveria ser 329.99");
        assertEquals(2, result.getPreco().scale(), "O preço deveria ter escala de 2 casas decimais");
    }
}
