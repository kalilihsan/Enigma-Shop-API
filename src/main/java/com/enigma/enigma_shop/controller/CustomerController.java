package com.enigma.enigma_shop.controller;

import com.enigma.enigma_shop.constant.APIUrl;
import com.enigma.enigma_shop.dto.request.SearchCustomerRequest;
import com.enigma.enigma_shop.entity.Customer;
import com.enigma.enigma_shop.service.CustomerService;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.CUSTOMER_API)
public class CustomerController {
    private final CustomerService customerService;
    @PostMapping
    public Customer createNewCustomer(@RequestBody Customer product) {
        return customerService.create(product);
    }

    @GetMapping(path = "/{id}")
    public Customer getCustomerById(@PathVariable String id) {
        return customerService.getById(id);
    }

    @GetMapping
    public List<Customer> getAllCustomer(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "mobilePhoneNo", required = false) String phoneNumber,
            @RequestParam(name = "birthDate", required = false) @JsonFormat(pattern = "yyyy-MM-dd") String birthDate,
            @RequestParam(name = "status", defaultValue = "true", required = false) Boolean status
    ) {
        SearchCustomerRequest request = SearchCustomerRequest.builder()
                .name(name)
                .mobilePhoneNumber(phoneNumber)
                .birthDate(birthDate)
                .status(status)
                .build();

        return customerService.getAll(request);
    }

    @PutMapping
    public Customer updateCustomer(@RequestBody Customer product) {
        return customerService.update(product);
    }

    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable String id) {
        customerService.deleteById(id);
        return "OK";
    }
}
