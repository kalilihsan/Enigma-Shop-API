package com.enigma.enigma_shop.service;

import com.enigma.enigma_shop.entity.Product;

import java.util.List;

public interface ProductService {
    Product create(Product product);
    Product getById(String id);
    List<Product> getAll(String name);
    Product update(Product product);
    void deleteById(String id);
}