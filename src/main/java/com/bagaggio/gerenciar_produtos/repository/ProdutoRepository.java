package com.bagaggio.gerenciar_produtos.repository;

import com.bagaggio.gerenciar_produtos.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório para operações de banco de dados relacionadas a produtos.
 */
@Repository
public interface ProdutoRepository extends JpaRepository<Produto,Long> {

}
