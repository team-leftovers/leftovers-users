package com.leftovers.user.services;

import com.leftovers.user.dto.CreateCustomerDto;
import com.leftovers.user.dto.UpdateCustomerDto;
import com.leftovers.user.exception.DuplicateEmailException;
import com.leftovers.user.exception.NoSuchCustomerException;
import com.leftovers.user.model.Address;
import com.leftovers.user.model.Customer;
import com.leftovers.user.repository.AddressRepository;
import com.leftovers.user.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NoSuchCustomerException(id));
    }

    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchCustomerException(email));
    }

    @Transactional
    public Customer createNewCustomer(CreateCustomerDto customerDto) {
        // Verify email isn't already registered
        customerRepository.findByEmail(customerDto.getEmail())
                .ifPresent(c -> {
                    throw new DuplicateEmailException(c.getEmail());
                });

        var address = Address.builder()
                // TODO(Jordan):
                //  Latitude and Longitude calculations
                //  (might be worth removing these values altogether, we'll see)
                .latitude(0.0)
                .longitude(0.0)

                .zipcode(customerDto.getZipcode())

                // TODO(Jordan):
                //  Right now we only support The US
                .country("US")

                .state(customerDto.getState())
                .city(customerDto.getCity())

                .streetAddress(customerDto.getAddressLine())
                .unitNumber(customerDto.getUnitNumber())
                .build();

        var customer = Customer.builder()
                .firstName(customerDto.getFirstName())
                .lastName(customerDto.getLastName())
                .email(customerDto.getEmail())
                .password(customerDto.getPassword())
                .phoneNo(customerDto.getPhoneNo())
                .address(addressRepository.save(address))
                .build();

        return customerRepository.save(customer);
    }

    public Customer updateCustomerAtId(Long id, UpdateCustomerDto customerDto) {
        var customer = customerRepository.findById(id).orElseThrow(() -> new NoSuchCustomerException(id));
        runIfNotNull(customerDto.getFirstName(), customerDto::setFirstName);
        runIfNotNull(customerDto.getLastName(), customerDto::setLastName);
        runIfNotNull(customerDto.getEmail(), customerDto::setEmail);
        runIfNotNull(customerDto.getPassword(), customerDto::setPassword);
        runIfNotNull(customerDto.getPhoneNo(), customerDto::setPhoneNo);
        runIfNotNull(customerDto.getAddressLine(), customer.getAddress()::setStreetAddress);
        runIfNotNull(customerDto.getUnitNumber(), customer.getAddress()::setUnitNumber);
        runIfNotNull(customerDto.getCity(), customer.getAddress()::setCity);
        runIfNotNull(customerDto.getState(), customer.getAddress()::setState);
        runIfNotNull(customerDto.getZipcode(), customer.getAddress()::setZipcode);

        customer.setAddress(addressRepository.save(customer.getAddress()));
        return customerRepository.save(customer);
    }

    private <T> void runIfNotNull(T val, Consumer<T> func) {
        if (val != null)
            func.accept(val);
    }

    public void removeCustomerById(Long id) {
        customerRepository.findById(id)
                .ifPresent(c -> {
                    customerRepository.delete(c);
                    addressRepository.delete(c.getAddress());
                });
    }
}
