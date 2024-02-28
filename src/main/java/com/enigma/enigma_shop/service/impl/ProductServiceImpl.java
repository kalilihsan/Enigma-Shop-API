package com.enigma.enigma_shop.service.impl;

import com.enigma.enigma_shop.dto.request.SearchProductRequest;
import com.enigma.enigma_shop.entity.Product;
import com.enigma.enigma_shop.repository.ProductRepository;
import com.enigma.enigma_shop.service.ProductService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public Page<Product> getAll(SearchProductRequest request) {
        if (request.getPage() <= 0) request.setPage(1);
        Pageable pageable = PageRequest.of((request.getPage() - 1), request.getSize());
        return productRepository.findAll(pageable);
    }

    @Override
    public Product update(Product product) {
        getById(product.getId());
        return productRepository.saveAndFlush(product);
    }

    @Override
    public void deleteById(String id) {
        Product currentProduct = getById(id);
        productRepository.delete(currentProduct);
    }
}
