package com.enigma.enigma_shop.controller;

import com.enigma.enigma_shop.constant.APIUrl;
import com.enigma.enigma_shop.constant.ResponseMessage;
import com.enigma.enigma_shop.dto.request.NewProductRequest;
import com.enigma.enigma_shop.dto.response.CommonResponse;
import com.enigma.enigma_shop.dto.response.ProductResponse;
import com.enigma.enigma_shop.service.ProductService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {
    @MockBean
    private ProductService productService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
    }

    @WithMockUser(username = "Kalil", roles = {"CUSTOMER"})
    @Test
    void shouldHave201StatusAndReturnCommonResponseWhenCreateNewProduct() throws Exception {
        NewProductRequest payload = NewProductRequest.builder()
                .name("KLX")
                .price(22000000L)
                .stock(100)
                .build();
        ProductResponse mockResponse = ProductResponse.builder()
                .id("Prod-1")
                .name(payload.getName())
                .price(payload.getPrice())
                .stock(payload.getStock())
                .build();

        Mockito.when(productService.create(Mockito.any())).thenReturn(mockResponse);

        String stringJson = objectMapper.writeValueAsString(payload);
        mockMvc.perform(
                MockMvcRequestBuilders.post(APIUrl.PRODUCT_API)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(stringJson)
        ).andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(result -> {
                    CommonResponse<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {});
                    assertEquals(201,response.getStatusCode());
                    assertEquals(ResponseMessage.SUCCESS_SAVE_DATA, response.getMessage());
                    assertEquals("Prod-1",response.getData().getId());
                });
    }

    @WithMockUser(username = "Kalil", roles = {"CUSTOMER"})
    @Test
    void shouldHave200StatusAndReturnResponseEntityWhenGetById() throws Exception {
        NewProductRequest payload = NewProductRequest.builder()
                .name("KLX")
                .price(22000000L)
                .stock(100)
                .build();
        ProductResponse mockResponse = ProductResponse.builder()
                .id("Prod-1")
                .name(payload.getName())
                .price(payload.getPrice())
                .stock(payload.getStock())
                .build();
        Mockito.when(productService.getOneById(Mockito.any())).thenReturn(mockResponse);

        mockMvc.perform(
                MockMvcRequestBuilders.get(APIUrl.PRODUCT_API+"/Prod-1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(MockMvcResultMatchers.status().isOk()).andDo(
                result -> {
                    CommonResponse<ProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<>() {});
                    assertEquals(200,response.getStatusCode());
                    assertEquals(ResponseMessage.SUCCESS_GET_DATA, response.getMessage());
                    assertEquals("Prod-1",response.getData().getId());
                    assertNotNull(response.getData());
                }
        );

    }

    @Test
    void getAllProduct() {
    }

    @Test
    void updateProduct() {
    }

    @Test
    void deleteById() {
    }
}