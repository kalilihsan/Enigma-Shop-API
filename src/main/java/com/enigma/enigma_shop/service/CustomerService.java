package com.enigma.enigma_shop.service;

import com.enigma.enigma_shop.dto.request.SearchCustomerRequest;
import com.enigma.enigma_shop.dto.response.CustomerResponse;
import com.enigma.enigma_shop.entity.Customer;

import java.util.List;

public interface CustomerService {
    CustomerResponse create(Customer customer);
    CustomerResponse getOneById(String id);
    Customer getById(String id);
    List<CustomerResponse> getAll(SearchCustomerRequest request);
    CustomerResponse update(Customer customer);
    void deleteById(String id);
    void updateStatusById(String id, Boolean status);
}
