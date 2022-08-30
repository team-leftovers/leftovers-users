package com.leftovers.user.controller;

import com.leftovers.user.dto.CreateCustomerDto;
import com.leftovers.user.dto.UpdateCustomerDto;
import com.leftovers.user.model.Customer;
import com.leftovers.user.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PreAuthorize("hasRole('ROLE_SITE_ADMIN')")
    @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.GET)
    public ResponseEntity<List<Customer>> getAllCustomers()
    {
        var customers = customerService.getAllCustomers();
        if (customers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(customers);
    }

    @PreAuthorize("hasRole('ROLE_SITE_ADMIN') || @authorizationService.isCustomer(authentication, #customerId)")
    @RequestMapping(value = "/{customerId}", produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.GET)
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerService.getCustomerById(customerId));
    }

    @PreAuthorize("hasRole('ROLE_SITE_ADMIN') || @authorizationService.isCustomer(authentication, #customerId)")
    @RequestMapping(value = "/{customerId}", produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.PUT)
    public ResponseEntity<Customer> updateCustomerAtId(@PathVariable Long customerId, @Valid @RequestBody UpdateCustomerDto customerDto) {
        var customer = customerService.updateCustomerAtId(customerId, customerDto);
        return ResponseEntity.ok(customer);
    }

    @PreAuthorize("hasRole('ROLE_SITE_ADMIN') || @authorizationService.isCustomer(authentication, #email)")
    @RequestMapping(value = "/email/{email}", produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.GET)
    public ResponseEntity<Customer> getCustomerByEmail(@PathVariable String email) {
        return ResponseEntity.ok(customerService.getCustomerByEmail(email));
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.POST)
    public ResponseEntity<Customer> createNewCustomer(@Valid @RequestBody CreateCustomerDto customerDto) {
        var customer = customerService.createNewCustomer(customerDto);

        return ResponseEntity.created(URI.create("/customers/" + customer.getId())).body(customer);
    }

    @PreAuthorize("hasRole('ROLE_SITE_ADMIN') || @authorizationService.isCustomer(authentication, #customerId)")
    @RequestMapping(value = "/{customerId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteCustomer(@PathVariable Long customerId) {
        customerService.removeCustomerById(customerId);
        return ResponseEntity.noContent().build();
    }
}
