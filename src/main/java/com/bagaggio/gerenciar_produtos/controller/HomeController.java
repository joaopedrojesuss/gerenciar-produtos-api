package com.bagaggio.gerenciar_produtos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller responsável por manipular requisições para a página inicial da aplicação.
 * Redireciona requisições da raiz ("/") para o endpoint de produtos ("/produtos").
 */
@Controller
public class HomeController {

    /**
     * Uma string de redirecionamento para o endpoint "/produtos"
     * Ao acessar "http://localhost:8080/", o usuário será automaticamente
     * redirecionado para "http://localhost:8080/produtos"
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/produtos";
    }
}
