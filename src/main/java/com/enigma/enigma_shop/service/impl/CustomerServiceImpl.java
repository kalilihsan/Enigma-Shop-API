package com.enigma.enigma_shop.service.impl;

import com.enigma.enigma_shop.dto.request.SearchCustomerRequest;
import com.enigma.enigma_shop.dto.response.CustomerResponse;
import com.enigma.enigma_shop.entity.Customer;
import com.enigma.enigma_shop.repository.CustomerRepository;
import com.enigma.enigma_shop.service.CustomerService;
import com.enigma.enigma_shop.specification.CustomerSpecification;
import com.enigma.enigma_shop.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ValidationUtil validationUtil;

    @Override
    public CustomerResponse create(Customer customer) {
        Customer newCustomer = customerRepository.saveAndFlush(customer);
        return convertCustomerToCustomerResponse(newCustomer);
    }

    @Override
    public CustomerResponse getOneById(String id) {
        return convertCustomerToCustomerResponse(findByIdOrThrowNotFound(id));
    }

    @Override
    public Customer getById(String id) {
        return findByIdOrThrowNotFound(id);
    }

    @Override
    public List<CustomerResponse> getAll(SearchCustomerRequest request) {
        Specification<Customer> specification = CustomerSpecification.getSpecification(request);
        return customerRepository.findAll(specification).stream().map(customer -> convertCustomerToCustomerResponse(customer)).toList();
    }

    @Override
    public CustomerResponse update(Customer customer) {
        findByIdOrThrowNotFound(customer.getId());
        Customer updatedCustomer = customerRepository.saveAndFlush(customer);
        return convertCustomerToCustomerResponse(updatedCustomer);
    }

    @Override
    public void deleteById(String id) {
        Customer customer = findByIdOrThrowNotFound(id);
        customerRepository.delete(customer);
    }

    @Override
    public void updateStatusById(String id, Boolean status) {
        findByIdOrThrowNotFound(id);
        customerRepository.updateStatus(id, status);
    }

    public Customer findByIdOrThrowNotFound(String id) {
        return customerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "customer not found"));
    }

    private CustomerResponse convertCustomerToCustomerResponse(Customer newCustomer) {
        return CustomerResponse.builder()
                .id(newCustomer.getId())
                .name(newCustomer.getName())
                .mobilePhoneNo(newCustomer.getMobilePhoneNo())
                .address(newCustomer.getAddress())
                .userAccountId(newCustomer.getUserAccount().getId())
                .build();
    }
}
