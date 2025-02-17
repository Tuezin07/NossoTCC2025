package com.caixarapido.controller;

import com.caixarapido.model.Produto;
import com.caixarapido.service.ProdutoService; // Nome corrigido
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/produtos") // Endpoint corrigido para "/produtos"
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService; // Nome corrigido

    @GetMapping("/{codigoBarras}") // Anotação corrigida
    public ResponseEntity<?> buscarPorCodigoBarras(@PathVariable String codigoBarras) {
        Produto produto = produtoService.buscarPorCodigoBarras(codigoBarras);
        if (produto == null) {
            // Retorna um JSON de erro com status 404
            Map<String, String> response = new HashMap<>();
            response.put("mensagem", "Produto não encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(produto); // Retorna o produto com status 200
    }
}