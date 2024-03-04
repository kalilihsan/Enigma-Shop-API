package com.enigma.enigma_shop.service.impl;

import com.enigma.enigma_shop.constant.ResponseMessage;
import com.enigma.enigma_shop.dto.request.NewProductRequest;
import com.enigma.enigma_shop.dto.request.SearchProductRequest;
import com.enigma.enigma_shop.dto.request.UpdateProductRequest;
import com.enigma.enigma_shop.dto.response.ProductResponse;
import com.enigma.enigma_shop.entity.Product;
import com.enigma.enigma_shop.repository.ProductRepository;
import com.enigma.enigma_shop.service.ProductService;
import com.enigma.enigma_shop.specification.ProductSpecification;
import com.enigma.enigma_shop.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductResponse create(NewProductRequest request) {
        validationUtil.validate(request);
        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .stock(request.getStock())
                .build();
        productRepository.saveAndFlush(product);
        return convertProductToProductResponse(product);
    }

    @Override
    public ProductResponse getOneById(String id) {
        Product product = findByIdOrThrowNotFound(id);
        return convertProductToProductResponse(product);
    }

    @Transactional(readOnly = true)
    @Override
    public Product getById(String id) {
        return findByIdOrThrowNotFound(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductResponse> getAll(SearchProductRequest request) {
        if (request.getPage() <= 0) request.setPage(1);

        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of((request.getPage() - 1), request.getSize(), sort);
        Specification<Product> specification = ProductSpecification.getSpecification(request);

        return productRepository.findAll(specification, pageable).map(this::convertProductToProductResponse);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductResponse update(UpdateProductRequest request) {
        Product currentProduct = findByIdOrThrowNotFound(request.getId());
        currentProduct.setName(request.getName());
        currentProduct.setPrice(request.getPrice());
        currentProduct.setStock(request.getStock());
        productRepository.saveAndFlush(currentProduct);
        return convertProductToProductResponse(currentProduct);
    }

    @Override
    public Product update(Product product) {
        return productRepository.saveAndFlush(product);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        Product currentProduct = getById(id);
        productRepository.delete(currentProduct);
    }

    private Product findByIdOrThrowNotFound(String id) {
        return productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
    }

    private ProductResponse convertProductToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }
}
