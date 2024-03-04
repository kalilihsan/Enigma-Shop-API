package com.enigma.enigma_shop.controller;

import com.enigma.enigma_shop.constant.APIUrl;
import com.enigma.enigma_shop.dto.request.SearchCustomerRequest;
import com.enigma.enigma_shop.dto.request.UpdateCustomerRequest;
import com.enigma.enigma_shop.dto.response.CustomerResponse;
import com.enigma.enigma_shop.entity.Customer;
import com.enigma.enigma_shop.service.CustomerService;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.CUSTOMER_API)
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping(path = "/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable String id) {
        CustomerResponse customer = customerService.getOneById(id);
        return ResponseEntity.ok(customer);
    }

    // hasAnyRole() -> multi role
    // hasRole() -> single role
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomer(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "mobilePhoneNo", required = false) String phoneNumber,
            @RequestParam(name = "birthDate", required = false) @JsonFormat(pattern = "yyyy-MM-dd") String birthDate,
            @RequestParam(name = "status", required = false) Boolean status
    ) {
        SearchCustomerRequest request = SearchCustomerRequest.builder()
                .name(name)
                .mobilePhoneNumber(phoneNumber)
                .birthDate(birthDate)
                .status(status)
                .build();

        List<CustomerResponse> customers = customerService.getAll(request);
        return ResponseEntity.ok(customers);
    }

    @PutMapping
    public ResponseEntity<CustomerResponse> updateCustomer(@RequestBody UpdateCustomerRequest payload) {
        CustomerResponse customer = customerService.update(payload);
        return ResponseEntity.ok(customer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateStatusCustomer(
            @PathVariable(name = "id") String id,
            @RequestParam(name = "status") Boolean status
    ) {
        customerService.updateStatusById(id, status);
        return ResponseEntity.ok("OK");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable String id) {
        customerService.deleteById(id);
        return ResponseEntity.ok("OK");
    }
}
