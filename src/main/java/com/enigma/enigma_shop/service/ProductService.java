package com.enigma.enigma_shop.service;

import com.enigma.enigma_shop.dto.request.SearchProductRequest;
import com.enigma.enigma_shop.entity.Product;
import org.springframework.data.domain.Page;

public interface ProductService {
    Product create(Product product);
    Product getById(String id);
    Page<Product> getAll(SearchProductRequest request);
    Product update(Product product);
    void deleteById(String id);
}
