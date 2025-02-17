package com.caixarapido.service;

import com.caixarapido.model.Venda;
import com.caixarapido.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VendaService {
    @Autowired
    private VendaRepository vendaRepository;

    public Venda salvarVenda(Venda venda) {
        return vendaRepository.save(venda);
    }
}