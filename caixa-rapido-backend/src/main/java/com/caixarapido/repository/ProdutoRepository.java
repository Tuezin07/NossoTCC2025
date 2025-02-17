package com.caixarapido.repository;

import com.caixarapido.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Produto findByCodigoBarras(String codigoBarras);
}