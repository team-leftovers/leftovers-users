package com.leftovers.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leftovers.user.dto.CreateCustomerDto;
import com.leftovers.user.dto.UpdateCustomerDto;
import com.leftovers.user.model.Address;
import com.leftovers.user.model.Customer;
import com.leftovers.user.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.validation.Validation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@Profile("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CustomerControllerTest {

    private final CustomerService customerService = Mockito.mock(CustomerService.class);
    private final CustomerController controller = new CustomerController(customerService);

    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final MockMvc mvc = MockMvcBuilders.standaloneSetup(controller)
            .build();

    private Customer validCustomer;
    private Customer validUpdatedCustomer;
    private CreateCustomerDto validCreateDto;
    private UpdateCustomerDto validUpdateDto;

    @BeforeEach
    void setUp() {
        Mockito.reset(customerService);

        var address = Address.builder()
                .id(1L)
                .latitude(0.0)
                .longitude(0.0)
                .zipcode("12345")
                .city("Test City")
                .state("TS")
                .country("US")
                .streetAddress("123 Test Lane")
                .unitNumber("456")
                .build();

        validCustomer = Customer.builder()
                .id(1L)
                .firstName("Test")
                .lastName("Case")
                .email("test.case@example.com")
                .phoneNo("1234567890")
                .password("this is a test password")
                .address(address)
                .build();

        validUpdatedCustomer = Customer.builder()
                .id(validCustomer.getId())
                .firstName(validCustomer.getFirstName())
                .lastName(validCustomer.getLastName())
                .email("test.case@test.com")
                .phoneNo(validCustomer.getPhoneNo())
                .password(validCustomer.getPassword())
                .address(address)
                .build();

        validCreateDto = CreateCustomerDto.builder()
                .firstName(validCustomer.getFirstName())
                .lastName(validCustomer.getLastName())
                .email(validCustomer.getEmail())
                .password(validCustomer.getPassword())
                .phoneNo(validCustomer.getPhoneNo())
                .addressLine(address.getStreetAddress())
                .unitNumber(address.getUnitNumber())
                .city(address.getCity())
                .state(address.getState())
                .zipcode(address.getZipcode())
                .build();

        validUpdateDto = UpdateCustomerDto.builder()
                .email(validUpdatedCustomer.getEmail())
                .build();
    }

    @Test
    void getAllCustomersReturnsListAndCode200() throws Exception {
        Mockito.when(customerService.getAllCustomers()).thenReturn(List.of(validCustomer));

        var result = mvc
                .perform(MockMvcRequestBuilders.get("/customers"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        var response = Arrays
                .stream(jsonMapper.readValue(result.getResponse().getContentAsString(), Customer[].class))
                .collect(Collectors.toList());

        assertEquals(List.of(validCustomer), response);
    }

    @Test
    void getAllCustomersReturnsEmptyListAndCode204() throws Exception {
        Mockito.when(customerService.getAllCustomers()).thenReturn(Collections.emptyList());

        mvc
                .perform(MockMvcRequestBuilders.get("/customers"))
                .andExpect(MockMvcResultMatchers.status().is(204));
    }

    @Test
    void getCustomerByIdReturnsValidCustomerAndCode200() throws Exception {
        Mockito.when(customerService.getCustomerById(validCustomer.getId())).thenReturn(validCustomer);

        var result = mvc
                .perform(MockMvcRequestBuilders.get("/customers/" + validCustomer.getId()))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        var response = jsonMapper
                .readValue(result.getResponse().getContentAsString(), Customer.class);

        assertEquals(validCustomer, response);
    }

    @Test
    void getCustomerByIdReturns404OnInvalidId() throws Exception {
        Mockito.when(customerService.getCustomerById(20L)).thenReturn(null);

        mvc
                .perform(MockMvcRequestBuilders.get("/customers/20"))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    void getCustomerByEmailReturnsValidCustomerAndCode200() throws Exception {
        Mockito.when(customerService.getCustomerByEmail(validCustomer.getEmail())).thenReturn(validCustomer);

        var result = mvc
                .perform(MockMvcRequestBuilders.get("/customers/email/" + validCustomer.getEmail()))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        var response = jsonMapper
                .readValue(result.getResponse().getContentAsString(), Customer.class);

        assertEquals(validCustomer, response);
    }

    @Test
    void getCustomerByEmailReturns404OnInvalidEmail() throws Exception {
        Mockito.when(customerService.getCustomerByEmail("fake@email.com")).thenReturn(null);

        mvc
                .perform(MockMvcRequestBuilders.get("/customers/email/fake@email.com"))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    void createNewCustomerReturnsCreatedIdAndCode201OnValidDto() throws Exception {
        Mockito.when(customerService.createNewCustomer(validCreateDto)).thenReturn(validCustomer);

        mvc
                .perform(
                        MockMvcRequestBuilders.post("/customers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonMapper.writeValueAsString(validCreateDto)))
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.header().string("Location", "/customers/" + validCustomer.getId()));
    }

    @Test
    void updateCustomerReturnsUpdatedCustomerAndCode200OnValidDto() throws Exception {
        Mockito.when(customerService.updateCustomerAtId(validCustomer.getId(), validUpdateDto)).thenReturn(validUpdatedCustomer);

        var result = mvc
                .perform(
                        MockMvcRequestBuilders.put("/customers/" + validCustomer.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonMapper.writeValueAsString(validUpdateDto)))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        var customer = jsonMapper.readValue(result.getResponse().getContentAsString(), Customer.class);

        assertEquals(validUpdatedCustomer, customer);
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
        validCreateDto.setFirstName(null);
        assertFalse(noValidationViolations(validCreateDto));

        validCreateDto.setFirstName("");
        assertFalse(noValidationViolations(validCreateDto));
    }

    @Test
    void createNewCustomerDisallowsInvalidLastName() {
        validCreateDto.setLastName(null);
        assertFalse(noValidationViolations(validCreateDto));

        validCreateDto.setLastName("");
        assertFalse(noValidationViolations(validCreateDto));
    }

    @Test
    void createNewCustomerDisallowsInvalidEmail() {
        validCreateDto.setEmail(null);
        assertFalse(noValidationViolations(validCreateDto));

        validCreateDto.setEmail("");
        assertFalse(noValidationViolations(validCreateDto));
    }

    @Test
    void createNewCustomerDisallowsInvalidPassword() {
        validCreateDto.setPassword(null);
        assertFalse(noValidationViolations(validCreateDto));

        validCreateDto.setPassword("");
        assertFalse(noValidationViolations(validCreateDto));

        validCreateDto.setPassword("short");
        assertFalse(noValidationViolations(validCreateDto));

        validCreateDto.setPassword(new String(new char[129]).replace('\0', '.'));
        assertFalse(noValidationViolations(validCreateDto));
    }

    @Test
    void createNewCustomerDisallowsInvalidPhoneNo() {
        validCreateDto.setPhoneNo(null);
        assertFalse(noValidationViolations(validCreateDto));

        validCreateDto.setPhoneNo("");
        assertFalse(noValidationViolations(validCreateDto));

        validCreateDto.setPhoneNo("Invalid");
        assertFalse(noValidationViolations(validCreateDto));
    }

    @Test
    void createNewCustomerDisallowsInvalidAddress() {
        validCreateDto.setAddressLine(null);
        assertFalse(noValidationViolations(validCreateDto));

        validCreateDto.setAddressLine("");
        assertFalse(noValidationViolations(validCreateDto));
    }

    @Test
    void createNewCustomerDisallowsInvalidCity() {
        validCreateDto.setCity(null);
        assertFalse(noValidationViolations(validCreateDto));

        validCreateDto.setCity("");
        assertFalse(noValidationViolations(validCreateDto));
    }

    @Test
    void createNewCustomerDisallowsInvalidState() {
        validCreateDto.setState(null);
        assertFalse(noValidationViolations(validCreateDto));

        validCreateDto.setState("");
        assertFalse(noValidationViolations(validCreateDto));

        validCreateDto.setState("Long");
        assertFalse(noValidationViolations(validCreateDto));
    }

    @Test
    void createNewCustomerDisallowsInvalidZipcode() {
        validCreateDto.setZipcode(null);
        assertFalse(noValidationViolations(validCreateDto));

        validCreateDto.setZipcode("");
        assertFalse(noValidationViolations(validCreateDto));

        validCreateDto.setZipcode("Hello");
        assertFalse(noValidationViolations(validCreateDto));
    }

    @Test
    void deleteCustomerReturnsCode204() throws Exception {
        mvc
                .perform(MockMvcRequestBuilders.delete("/customers/" + validCustomer.getId()))
                .andExpect(MockMvcResultMatchers.status().is(204));
    }
}