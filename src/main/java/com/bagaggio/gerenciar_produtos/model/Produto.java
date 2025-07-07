package com.bagaggio.gerenciar_produtos.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


/**
 * Entidade que representa um produto no sistema.
 * Mapeada para a tabela "tb_produtos" no banco de dados.
 */
@Entity
@Table(name = "tb_produtos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,  length = 100)
    private String nome;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal preco;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false, length = 50)
    private String categoria;
}

