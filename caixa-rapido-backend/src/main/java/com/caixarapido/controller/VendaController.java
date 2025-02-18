package com.caixarapido.controller;

import com.caixarapido.service.MercadoPagoService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/vendas")
public class VendaController {

    private final MercadoPagoService mercadoPagoService;
    private final ObjectMapper objectMapper = new ObjectMapper(); // Para manipular JSON

    public VendaController(MercadoPagoService mercadoPagoService) {
        this.mercadoPagoService = mercadoPagoService;
    }

    @PostMapping
    public ResponseEntity<?> criarVenda(@RequestBody Map<String, Object> payload) {
        try {
            // Obtendo valores do JSON enviado pelo frontend
            BigDecimal valor = new BigDecimal(payload.get("valorTotal").toString());
            String descricao = "Pagamento via PIX";

            // Chamando o serviço para criar o pagamento PIX no Mercado Pago
            String respostaJson = mercadoPagoService.criarPagamentoPix(valor, descricao);

            // Convertendo a resposta JSON para um objeto manipulável
            JsonNode jsonResponse = objectMapper.readTree(respostaJson);

            // Obtendo a URL do QR Code PIX
            String urlPagamento = jsonResponse.path("point_of_interaction")
                                              .path("transaction_data")
                                              .path("ticket_url")
                                              .asText();

            // Se a URL não for encontrada, retorna um erro
            if (urlPagamento == null || urlPagamento.isEmpty()) {
                return ResponseEntity.status(500).body(Map.of("error", "URL de pagamento PIX não encontrada."));
            }

            // Criando a resposta JSON para o frontend
            Map<String, String> response = new HashMap<>();
            response.put("message", "Pagamento criado com sucesso");
            response.put("urlPagamento", urlPagamento);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erro ao criar venda", "detalhes", e.getMessage()));
        }
    }
}
