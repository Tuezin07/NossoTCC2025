package com.caixarapido.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MercadoPagoService {

    private static final String ACCESS_TOKEN = "TEST-8532461171043214-021218-f3052b0fb0d0b469be01cd3a86d6eeb8-148553770";
    private static final String API_URL = "https://api.mercadopago.com/v1/payments";

    public String criarPagamentoPix(double valor, String descricao) {
        RestTemplate restTemplate = new RestTemplate();

        // Cabeçalhos da requisição
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + ACCESS_TOKEN);

        // Corpo da requisição
        String requestBody = String.format(
            "{\"transaction_amount\": %.2f, \"payment_method_id\": \"pix\", \"description\": \"%s\", \"payer\"}}",
            valor, descricao
        )

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // Faz a requisição à API do Mercado Pago
        ResponseEntity<String> response = restTemplate.postForEntity(API_URL, request, String.class);

        if (response.getStatusCode() == HttpStatus.CREATED) {
            return response.getBody(); // Retorna a resposta da API
        } else {
            throw new RuntimeException("Falha ao criar pagamento PIX. Status: " + response.getStatusCode());
        }
    }
}
