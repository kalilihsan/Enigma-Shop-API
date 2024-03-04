package com.enigma.enigma_shop.controller;

import com.enigma.enigma_shop.constant.APIUrl;
import com.enigma.enigma_shop.constant.ResponseMessage;
import com.enigma.enigma_shop.dto.request.SearchCustomerRequest;
import com.enigma.enigma_shop.dto.request.UpdateCustomerRequest;
import com.enigma.enigma_shop.dto.response.CommonResponse;
import com.enigma.enigma_shop.dto.response.CustomerResponse;
import com.enigma.enigma_shop.entity.Customer;
import com.enigma.enigma_shop.service.CustomerService;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.CUSTOMER_API)
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping(path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<CustomerResponse>> getCustomerById(@PathVariable String id) {
        CustomerResponse customer = customerService.getOneById(id);
        CommonResponse<CustomerResponse> response = CommonResponse.<CustomerResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(customer)
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<List<CustomerResponse>>> getAllCustomer(
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

        CommonResponse<List<CustomerResponse>> response = CommonResponse.<List<CustomerResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(customers)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<CustomerResponse>> updateCustomer(@RequestBody UpdateCustomerRequest payload) {
        CustomerResponse customer = customerService.update(payload);
        CommonResponse<CustomerResponse> response = CommonResponse.<CustomerResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .data(customer)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<String>> updateStatusCustomer(
            @PathVariable(name = "id") String id,
            @RequestParam(name = "status") Boolean status
    ) {
        customerService.updateStatusById(id, status);
        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<String>> deleteById(@PathVariable String id) {
        customerService.deleteById(id);
        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_DELETE_DATA)
                .build();
        return ResponseEntity.ok(response);
    }
}
