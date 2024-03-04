package com.enigma.enigma_shop.service.impl;

import com.enigma.enigma_shop.constant.ResponseMessage;
import com.enigma.enigma_shop.dto.request.SearchCustomerRequest;
import com.enigma.enigma_shop.dto.request.UpdateCustomerRequest;
import com.enigma.enigma_shop.dto.response.CustomerResponse;
import com.enigma.enigma_shop.entity.Customer;
import com.enigma.enigma_shop.entity.UserAccount;
import com.enigma.enigma_shop.repository.CustomerRepository;
import com.enigma.enigma_shop.service.CustomerService;
import com.enigma.enigma_shop.service.UserService;
import com.enigma.enigma_shop.specification.CustomerSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final UserService userService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Customer create(Customer customer) {
        return customerRepository.saveAndFlush(customer);
    }

    @Transactional(readOnly = true)
    @Override
    public CustomerResponse getOneById(String id) {
        return convertCustomerToCustomerResponse(findByIdOrThrowNotFound(id));
    }

    @Transactional(readOnly = true)
    @Override
    public Customer getById(String id) {
        return findByIdOrThrowNotFound(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CustomerResponse> getAll(SearchCustomerRequest request) {
        Specification<Customer> specification = CustomerSpecification.getSpecification(request);
        return customerRepository.findAll(specification).stream().map(this::convertCustomerToCustomerResponse).toList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CustomerResponse update(UpdateCustomerRequest request) {
        Customer currentCustomer = findByIdOrThrowNotFound(request.getId());
        UserAccount userAccount = userService.getByContext();

        if (!userAccount.getId().equals(currentCustomer.getUserAccount().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ResponseMessage.ERROR_FORBIDDEN);
        }

        currentCustomer.setName(request.getName());
        currentCustomer.setMobilePhoneNo(request.getMobilePhoneNo());
        currentCustomer.setAddress(request.getAddress());
        currentCustomer.setBirthDate(Date.valueOf(request.getBirthDate()));
        customerRepository.saveAndFlush(currentCustomer);
        return convertCustomerToCustomerResponse(currentCustomer);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        Customer customer = findByIdOrThrowNotFound(id);
        customerRepository.delete(customer);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStatusById(String id, Boolean status) {
        findByIdOrThrowNotFound(id);
        customerRepository.updateStatus(id, status);
    }

    private Customer findByIdOrThrowNotFound(String id) {
        return customerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
    }

    private CustomerResponse convertCustomerToCustomerResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .mobilePhoneNo(customer.getMobilePhoneNo())
                .address(customer.getAddress())
                .status(customer.getStatus())
                .userAccountId(customer.getUserAccount().getId())
                .build();
    }
}
