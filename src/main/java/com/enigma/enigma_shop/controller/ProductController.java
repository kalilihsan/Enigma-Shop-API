package com.enigma.enigma_shop.controller;

import com.enigma.enigma_shop.constant.APIUrl;
import com.enigma.enigma_shop.constant.ResponseMessage;
import com.enigma.enigma_shop.dto.request.NewProductRequest;
import com.enigma.enigma_shop.dto.request.SearchProductRequest;
import com.enigma.enigma_shop.dto.request.UpdateProductRequest;
import com.enigma.enigma_shop.dto.response.CommonResponse;
import com.enigma.enigma_shop.dto.response.PagingResponse;
import com.enigma.enigma_shop.dto.response.ProductResponse;
import com.enigma.enigma_shop.entity.Product;
import com.enigma.enigma_shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.PRODUCT_API)
public class ProductController {
    private final ProductService productService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<ProductResponse>> createNewProduct(@RequestBody NewProductRequest request) {
        ProductResponse newProduct = productService.create(request);
        CommonResponse<ProductResponse> response = CommonResponse.<ProductResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(newProduct)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<ProductResponse>> getProductById(@PathVariable String id) {
            ProductResponse product = productService.getOneById(id);
        CommonResponse<ProductResponse> response = CommonResponse.<ProductResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(product)
                .build();
            return ResponseEntity.ok(response);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<List<ProductResponse>>> getAllProduct(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "name", required = false) String name
    ) {
        SearchProductRequest request = SearchProductRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .name(name)
                .build();
        Page<ProductResponse> products = productService.getAll(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(products.getTotalPages())
                .totalElement(products.getTotalElements())
                .page(products.getPageable().getPageNumber() + 1)
                .size(products.getPageable().getPageSize())
                .hasNext(products.hasNext())
                .hasPrevious(products.hasPrevious())
                .build();

        CommonResponse<List<ProductResponse>> response = CommonResponse.<List<ProductResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(products.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<ProductResponse>> updateProduct(@RequestBody UpdateProductRequest request) {
        ProductResponse updatedProduct = productService.update(request);
        CommonResponse<ProductResponse> response = CommonResponse.<ProductResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .data(updatedProduct)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<?>> deleteById(@PathVariable String id) {
        productService.deleteById(id);
        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_DELETE_DATA)
                .build();
        return ResponseEntity.ok(response);
    }
}
