package com.leftovers.user.controller;

import com.leftovers.user.dto.CreateCustomerDto;
import com.leftovers.user.model.Address;
import com.leftovers.user.model.Customer;
import com.leftovers.user.repository.AddressRepository;
import com.leftovers.user.repository.CustomerRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;

    public CustomerController(CustomerRepository customerRepository, AddressRepository addressRepository) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Customer>> getAllCustomers()
    {
        var customers = (List<Customer>)customerRepository.findAll();
        if (customers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(customers);
    }

    @GetMapping(value = "/{customerId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long customerId) {
        return ResponseEntity.of(customerRepository.findById(customerId));
    }

    @GetMapping(value = "/email/{email}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Customer> getCustomerByEmail(@PathVariable String email) {
        return ResponseEntity.of(customerRepository.findByEmail(email));
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Customer> createNewCustomer(@Valid @RequestBody CreateCustomerDto customerDto) {
        // Verify email isn't already registered
        customerRepository.findByEmail(customerDto.email)
                .ifPresent(c -> {
                    throw new RuntimeException("Email is already registered");
                });

        var address = new Address();

        // TODO(Jordan):
        //  Latitude and Longitude calculations
        //  (might be worth removing these values altogether, we'll see)
        address.setLatitude(0.0);
        address.setLongitude(0.0);

        // TODO(Jordan):
        //  Update Database to allow for extended zipcodes (#####-####)
        address.setZipCode(Integer.parseInt(customerDto.zipcode));

        // TODO(Jordan):
        //  Right now we only support The US
        address.setCountry("US");

        // TODO(Jordan):
        //  Ask team about state handling
        // address.setState(customerDto.state);

        if (customerDto.houseNumber != null && !customerDto.houseNumber.isBlank())
            address.setHouseNumber(customerDto.houseNumber);

        if (customerDto.unitNumber != null && !customerDto.unitNumber.isBlank())
            address.setUnitNumber(customerDto.unitNumber);

        address.setStreetAddress(customerDto.addressLine);

        var customer = new Customer(addressRepository.save(address));
        customer.setFirstName(customerDto.firstName);
        customer.setLastName(customerDto.lastName);
        customer.setEmail(customerDto.email);
        customer.setHashedPassword(customerDto.password);
        customer.setPhoneNo(customerDto.phoneNo);

        var newCustomer = customerRepository.save(customer);

        return ResponseEntity.created(URI.create("/customer/" + newCustomer.getId())).body(newCustomer);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long customerId) {
        customerRepository.findById(customerId).ifPresent(c -> {
            customerRepository.delete(c);
            addressRepository.delete(c.getAddress());
        });
        return ResponseEntity.noContent().build();
    }
}
