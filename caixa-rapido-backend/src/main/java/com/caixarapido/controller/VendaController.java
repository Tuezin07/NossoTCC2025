package com.caixarapido.controller;

import com.caixarapido.model.Venda;
import com.caixarapido.service.MercadoPagoService;
import com.caixarapido.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vendas")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @Autowired
    private MercadoPagoService mercadoPagoService;

    @PostMapping
    public String salvarVenda(@RequestBody Venda venda) {
        vendaService.salvarVenda(venda);

        try {
            // Gera o QR Code PIX
            String qrCode = mercadoPagoService.criarPagamentoPix(venda.getValorTotal());
            return qrCode; // Retorna o QR Code em base64
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao processar pagamento via PIX";
        }
    }
}