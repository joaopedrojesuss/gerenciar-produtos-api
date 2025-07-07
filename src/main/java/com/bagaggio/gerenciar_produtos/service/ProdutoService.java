package com.bagaggio.gerenciar_produtos.service;


import com.bagaggio.gerenciar_produtos.dto.ProdutoDTO;
import com.bagaggio.gerenciar_produtos.model.Produto;
import com.bagaggio.gerenciar_produtos.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço responsável pela lógica de negócios relacionada a produtos.
 */
@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    @Autowired
    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    /**
     * Lista todos os produtos cadastrados
     */
    public List<ProdutoDTO> listarTodos() {
        return produtoRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * Busca um produto por ID
     * @throws RuntimeException se o produto não for encontrado
     */
    public ProdutoDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado!"));
        return toDTO(produto);
    }

    /**
     * Cria um novo produto
     */
    public ProdutoDTO criarProduto(ProdutoDTO produtoDTO) {
        Produto produto = toEntity(produtoDTO);
        produto = produtoRepository.save(produto);
        return toDTO(produto);
    }

    /**
     * Atualiza um produto existente
     * @throws RuntimeException se o produto não for encontrado
     */
    public ProdutoDTO atualizarProduto(Long id, ProdutoDTO produtoDTO) {
        Produto produtoExistente = produtoRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado!"));
        produtoExistente.setNome(produtoDTO.getNome());
        produtoExistente.setDescricao(produtoDTO.getDescricao());
        produtoExistente.setPreco(produtoDTO.getPreco());
        produtoExistente.setQuantidade(produtoDTO.getQuantidade());

        produtoExistente = produtoRepository.save(produtoExistente);
        return toDTO(produtoExistente);
    }

    /**
     * Remove um produto
     * @return true se o produto foi removido, false se não existir
     */
    public boolean deletarProduto(Long id) {
        if(!produtoRepository.existsById(id)) {
            return false;
        }
        produtoRepository.deleteById(id);
        return true;
    }

    // Métodos auxiliares de conversão
    private Produto toEntity(ProdutoDTO produtoDTO) {
        return Produto.builder()
                .nome(produtoDTO.getNome())
                .descricao((produtoDTO.getDescricao()))
                .preco(produtoDTO.getPreco().setScale(2, RoundingMode.HALF_EVEN))
                .quantidade(produtoDTO.getQuantidade())
                .categoria(produtoDTO.getCategoria())
                .build();
    }

    private ProdutoDTO toDTO(Produto produto) {
        return new ProdutoDTO(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getQuantidade(),
                produto.getCategoria()
        );
    }
}
