package com.enigma.enigma_shop.service.impl;

import com.enigma.enigma_shop.dto.request.SearchCustomerRequest;
import com.enigma.enigma_shop.dto.response.CustomerResponse;
import com.enigma.enigma_shop.entity.Customer;
import com.enigma.enigma_shop.entity.UserAccount;
import com.enigma.enigma_shop.repository.CustomerRepository;
import com.enigma.enigma_shop.service.CustomerService;
import com.enigma.enigma_shop.specification.CustomerSpecification;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;
    private CustomerService customerService;
    @BeforeEach
    void setUp() {
        customerService = new CustomerServiceImpl(customerRepository);
    }
    @Test
    void shouldReturnCustomerWhenCreate() {
        Customer parameterCustomer = Customer.builder()
                .id("Customer-1")
                .name("Cahyo")
                .mobilePhoneNo("08113865625")
                .address("Jalan Topaz 7")
                .birthDate(new Date())
                .status(true)
                .userAccount(UserAccount.builder().build())
                .build();

        Mockito.when(customerRepository.saveAndFlush(parameterCustomer)).thenReturn(parameterCustomer);

        Assertions.assertNotNull(customerService.create(parameterCustomer));
    }

    @Test
    void shouldReturnCustomerResponseWhenGetOneById() {
        String id = "Customer-1";

        Customer mockReturn = Customer.builder()
                .id("Customer-1")
                .name("Cahyo")
                .mobilePhoneNo("08113865625")
                .address("Jalan Topaz 7")
                .birthDate(new Date())
                .status(true)
                .userAccount(UserAccount.builder().build())
                .build();
        Mockito.when(customerRepository.findById(id))
                .thenReturn(Optional.of(mockReturn));

        CustomerResponse actualCustomerResponse = customerService.getOneById(id);

        assertEquals("Customer-1",actualCustomerResponse.getId());
        assertEquals("Cahyo",actualCustomerResponse.getName());
        Mockito.verify(customerRepository,Mockito.times(1)).findById(id);

    }

    @Test
    void shouldReturnCustomerWhenGetById() {
        String id = "Customer-1";

        Customer mockReturn = Customer.builder()
                .id("Customer-1")
                .name("Cahyo")
                .mobilePhoneNo("08113865625")
                .address("Jalan Topaz 7")
                .birthDate(new Date())
                .status(true)
                .userAccount(UserAccount.builder().build())
                .build();
        Mockito.when(customerRepository.findById(id))
                .thenReturn(Optional.of(mockReturn));

        Customer customer = customerService.getById(id);

        assertNotNull(customer);
        assertEquals(id,customer.getId());
        Mockito.verify(customerRepository,Mockito.times(1)).findById(id);
    }

    @Test
    void shouldReturnCustomerResponsesWhenGetAll() {
        SearchCustomerRequest request = SearchCustomerRequest.builder().build();
        Specification<Customer> specification = CustomerSpecification.getSpecification(request);

        List<Customer> customers = List.of(
                Customer.builder().id("Customer-1").name("Kalil").userAccount(UserAccount.builder().build()).build(),
                Customer.builder().id("Customer-2").name("Cahyo").userAccount(UserAccount.builder().build()).build(),
                Customer.builder().id("Customer-3").name("Ilham").userAccount(UserAccount.builder().build()).build()
        );

        Mockito.when(customerRepository.findAll(Mockito.any(Specification.class))).thenReturn(customers);

        List<CustomerResponse> customerResponses = customerService.getAll(request);

        assertNotNull(customerResponses);
        assertEquals(customers.size(),customerResponses.size());
    }

    @Test
    void shouldDeleteByIdSuccessfully() {
        String id = "Customer-1";
        Optional<Customer> customer =Optional.of(Customer.builder().id(id).name("Kalil").build());
        Mockito.when(customerRepository.findById(id)).thenReturn(customer);
        Mockito.doNothing().when(customerRepository).delete(customer.get());

        customerService.deleteById(id);

        Mockito.verify(customerRepository,Mockito.times(1))
                .delete(customer.get());
    }

    @Test
    void deleteById() {
    }

    @Test
    void updateStatusById() {
        String id = "Customer-1";
        Optional<Customer> customer = Optional.of(Customer.builder().id(id).name("Kalil").status(true).build());
        Mockito.when(customerRepository.findById(id)).thenReturn(customer);
        Mockito.doNothing().when(customerRepository).updateStatus(id, false);

        customerService.updateStatusById(id,false);

        Mockito.verify(customerRepository,Mockito.times(1)).updateStatus(id,false);
    }
}