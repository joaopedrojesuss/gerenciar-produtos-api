package com.bagaggio.gerenciar_produtos.dto;

import jakarta.validation.constraints.*;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO (Data Transfer Object) para a entidade Produto.
 * Contém as validações dos campos e é usado para comunicação com a API.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoDTO {
    private Long id;

    @NotBlank(message = "O nome é obrigatório!")
    @Size(max = 100, message = "O nome não pode exceder 100 caracteres")
    private String nome;

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @NotNull(message = "O preço é obrigatório")
    @DecimalMin(value="0.0", inclusive = false, message = "O preço deve ser positivo")
    @Digits(integer = 16, fraction = 2)
    private BigDecimal preco;

    @PositiveOrZero(message = "A quantidade não pode ser negativa")
    private Integer quantidade;

    @NotBlank(message = "A categoria é obrigatória")
    @Size(max = 50, message = "A categoria não pode exceder 50 caracteres")
    private String categoria;


}
