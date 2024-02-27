package com.enigma.enigma_shop.service.impl;

import com.enigma.enigma_shop.dto.request.SearchCustomerRequest;
import com.enigma.enigma_shop.entity.Customer;
import com.enigma.enigma_shop.repository.CustomerRepository;
import com.enigma.enigma_shop.service.CustomerService;
import com.enigma.enigma_shop.specification.CustomerSpecification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public Customer create(Customer customer) {
        return customerRepository.saveAndFlush(customer);
    }

    @Override
    public Customer getById(String id) {
        return findByIdOrThrowNotFound(id);
    }

    @Override
    public List<Customer> getAll(SearchCustomerRequest request) {
        Specification<Customer> specification = CustomerSpecification.getSpecification(request);
        return customerRepository.findAll(specification);
    }

    @Override
    public Customer update(Customer customer) {
        findByIdOrThrowNotFound(customer.getId());
        return customerRepository.saveAndFlush(customer);
    }

    @Override
    public void deleteById(String id) {
        Customer customer = findByIdOrThrowNotFound(id);
        customerRepository.delete(customer);
    }

    public Customer findByIdOrThrowNotFound(String id) {
        return customerRepository.findById(id).orElseThrow(() -> new RuntimeException("customer not found"));
    }
}
