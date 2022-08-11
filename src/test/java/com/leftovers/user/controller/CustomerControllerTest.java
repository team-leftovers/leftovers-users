package com.leftovers.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leftovers.user.dto.CreateCustomerDto;
import com.leftovers.user.model.Address;
import com.leftovers.user.model.Customer;
import com.leftovers.user.repository.AddressRepository;
import com.leftovers.user.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.validation.Validation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.*;

@Profile("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CustomerControllerTest {

    private final CustomerRepository customerRepository = Mockito.mock(CustomerRepository.class);
    private final AddressRepository addressRepository = Mockito.mock(AddressRepository.class);
    private final CustomerController controller = new CustomerController(customerRepository, addressRepository);

    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final MockMvc mvc = MockMvcBuilders.standaloneSetup(controller)
            .build();

    private Customer validCustomer;
    private CreateCustomerDto validDto;

    @BeforeEach
    void setUp() {
        Mockito.reset(customerRepository, addressRepository);

        validCustomer = new Customer();

        validCustomer.setId(1L);
        validCustomer.setFirstName("Test");
        validCustomer.setLastName("Case");
        validCustomer.setEmail("test.case@example.com");
        validCustomer.setPhoneNo("1234567890");
        validCustomer.setHashedPassword("this is a test password");

        Address address = new Address();
        address.setId(1L);
        address.setLatitude(0.0);
        address.setLongitude(0.0);
        address.setZipCode(12345);
        address.setCountry("US");
        address.setStreetAddress("Test Lane");
        address.setHouseNumber("123");
        address.setUnitNumber("456");

        validCustomer.setAddress(address);

        validDto = new CreateCustomerDto();
        validDto.firstName = validCustomer.getFirstName();
        validDto.lastName = validCustomer.getLastName();
        validDto.email = validCustomer.getEmail();
        validDto.password = validCustomer.getHashedPassword();
        validDto.phoneNo = validCustomer.getPhoneNo();
        validDto.addressLine = address.getStreetAddress();
        validDto.houseNumber = address.getHouseNumber();
        validDto.unitNumber = address.getUnitNumber();
        validDto.city = "Test City";
        validDto.state = "TS";
        validDto.zipcode = address.getZipCode().toString();
    }

    @Test
    void getAllCustomersReturnsListAndCode200() throws Exception {
        Mockito.when(customerRepository.findAll()).thenReturn(List.of(validCustomer));

        var result = mvc
                .perform(MockMvcRequestBuilders.get("/customer"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        var response = Arrays
                .stream(jsonMapper.readValue(result.getResponse().getContentAsString(), Customer[].class))
                .collect(Collectors.toList());

        assertEquals(List.of(validCustomer), response);
    }

    @Test
    void getAllCustomersReturnsEmptyListAndCode204() throws Exception {
        Mockito.when(customerRepository.findAll()).thenReturn(Collections.emptyList());

        mvc
                .perform(MockMvcRequestBuilders.get("/customer"))
                .andExpect(MockMvcResultMatchers.status().is(204));
    }

    @Test
    void getCustomerByIdReturnsValidCustomerAndCode200() throws Exception {
        Mockito.when(customerRepository.findById(validCustomer.getId())).thenReturn(Optional.of(validCustomer));

        var result = mvc
                .perform(MockMvcRequestBuilders.get("/customer/" + validCustomer.getId()))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        var response = jsonMapper
                .readValue(result.getResponse().getContentAsString(), Customer.class);

        assertEquals(validCustomer, response);
    }

    @Test
    void getCustomerByIdReturns404OnInvalidId() throws Exception {
        Mockito.when(customerRepository.findById(20L)).thenReturn(Optional.empty());

        mvc
                .perform(MockMvcRequestBuilders.get("/customer/20"))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    void getCustomerByEmailReturnsValidCustomerAndCode200() throws Exception {
        Mockito.when(customerRepository.findByEmail(validCustomer.getEmail())).thenReturn(Optional.of(validCustomer));

        var result = mvc
                .perform(MockMvcRequestBuilders.get("/customer/email/" + validCustomer.getEmail()))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        var response = jsonMapper
                .readValue(result.getResponse().getContentAsString(), Customer.class);

        assertEquals(validCustomer, response);
    }

    @Test
    void getCustomerByEmailReturns404OnInvalidEmail() throws Exception {
        Mockito.when(customerRepository.findByEmail("fake@email.com")).thenReturn(Optional.empty());

        mvc
                .perform(MockMvcRequestBuilders.get("/customer/email/fake@email.com"))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    void createNewCustomerReturnsCreatedIdAndCode201OnValidDto() throws Exception {
        Mockito.when(customerRepository.save(Mockito.any(Customer.class))).thenReturn(validCustomer);

        mvc
                .perform(
                        MockMvcRequestBuilders.post("/customer")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonMapper.writeValueAsString(validDto)))
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.header().string("Location", "/customer/" + validCustomer.getId()));
    }

    boolean noValidationViolations(CreateCustomerDto createCustomerDto)
    {
        return Validation.buildDefaultValidatorFactory()
                .getValidator()
                .validate(createCustomerDto)
                .isEmpty();
    }

    @Test
    void createNewCustomerDisallowsInvalidFirstName() {
        validDto.firstName = null;
        assertFalse(noValidationViolations(validDto));

        validDto.firstName = "";
        assertFalse(noValidationViolations(validDto));
    }

    @Test
    void createNewCustomerDisallowsInvalidLastName() {
        validDto.lastName = null;
        assertFalse(noValidationViolations(validDto));

        validDto.lastName = "";
        assertFalse(noValidationViolations(validDto));
    }

    @Test
    void createNewCustomerDisallowsInvalidEmail() {
        validDto.email = null;
        assertFalse(noValidationViolations(validDto));

        validDto.email = "";
        assertFalse(noValidationViolations(validDto));
    }

    @Test
    void createNewCustomerDisallowsInvalidPassword() {
        validDto.password = null;
        assertFalse(noValidationViolations(validDto));

        validDto.password = "";
        assertFalse(noValidationViolations(validDto));

        validDto.password = "short";
        assertFalse(noValidationViolations(validDto));

        validDto.password = new String(new char[129]).replace('\0', '.');
        assertFalse(noValidationViolations(validDto));
    }

    @Test
    void createNewCustomerDisallowsInvalidPhoneNo() {
        validDto.phoneNo = null;
        assertFalse(noValidationViolations(validDto));

        validDto.phoneNo = "";
        assertFalse(noValidationViolations(validDto));

        validDto.phoneNo = "Invalid";
        assertFalse(noValidationViolations(validDto));
    }

    @Test
    void createNewCustomerDisallowsInvalidAddress() {
        validDto.addressLine = null;
        assertFalse(noValidationViolations(validDto));

        validDto.addressLine = "";
        assertFalse(noValidationViolations(validDto));
    }

    @Test
    void createNewCustomerDisallowsInvalidCity() {
        validDto.city = null;
        assertFalse(noValidationViolations(validDto));

        validDto.city = "";
        assertFalse(noValidationViolations(validDto));
    }

    @Test
    void createNewCustomerDisallowsInvalidState() {
        validDto.state = null;
        assertFalse(noValidationViolations(validDto));

        validDto.state = "";
        assertFalse(noValidationViolations(validDto));

        validDto.state = "Long";
        assertFalse(noValidationViolations(validDto));
    }

    @Test
    void createNewCustomerDisallowsInvalidZipcode() {
        validDto.zipcode = null;
        assertFalse(noValidationViolations(validDto));

        validDto.zipcode = "";
        assertFalse(noValidationViolations(validDto));

        validDto.zipcode = "Hello";
        assertFalse(noValidationViolations(validDto));
    }

    @Test
    void deleteCustomerReturnsCode204() throws Exception {
        mvc
                .perform(MockMvcRequestBuilders.delete("/customer/" + validCustomer.getId()))
                .andExpect(MockMvcResultMatchers.status().is(204));
    }
}