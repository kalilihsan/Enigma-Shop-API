package com.enigma.enigma_shop.service;

import com.enigma.enigma_shop.dto.request.NewProductRequest;
import com.enigma.enigma_shop.dto.request.SearchProductRequest;
import com.enigma.enigma_shop.dto.request.UpdateProductRequest;
import com.enigma.enigma_shop.dto.response.ProductResponse;
import com.enigma.enigma_shop.entity.Product;
import org.springframework.data.domain.Page;

public interface ProductService {
    ProductResponse create(NewProductRequest request);
    ProductResponse getOneById(String id);
    Product getById(String id);
    Page<ProductResponse> getAll(SearchProductRequest request);
    ProductResponse update(UpdateProductRequest request);
    Product update(Product product);
    void deleteById(String id);
}
