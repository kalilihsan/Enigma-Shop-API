package com.enigma.enigma_shop.service.impl;

import com.enigma.enigma_shop.entity.Product;
import com.enigma.enigma_shop.repository.ProductRepository;
import com.enigma.enigma_shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


// lombok RequiredArgumentConstructor -> constructor injection
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public Product create(Product product) {
        return productRepository.saveAndFlush(product);
    }

    @Override
    public Product getById(String id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) throw new RuntimeException("product not found");
        return optionalProduct.get();
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Product update(Product product) {
        getById(product.getId());
        return productRepository.save(product);
    }

    @Override
    public void delete(String id) {
        Product currentProduct = getById(id);
        productRepository.delete(currentProduct);
    }
}
