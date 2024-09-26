package com.amigoscode.testing.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

class CustomerRegistrationServiceTest {

    private CustomerRegistrationService underTest;

    @Captor
    private ArgumentCaptor<Customer> customerArgumentCaptor;
    @Mock
    private  CustomerRepository customerRepository;

    @BeforeEach
    void setup(){
        MockitoAnnotations.initMocks(this);
        underTest = new CustomerRegistrationService(customerRepository) {
        };
    }

    @Test
    void itShouldSaveNewCustomer() {
        // Given
        String phoneNumber = "00000";
        Customer customer = new Customer(UUID.randomUUID(), "Maryan", phoneNumber);

        // .. a request

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        given(customerRepository.selectCustomerNumberByPhoneNumber(phoneNumber))
                .willReturn(Optional.empty());

        // When
        underTest.registerNewCustomer(request);

        // Then

        then(customerRepository).should().save(customerArgumentCaptor.capture());
        Customer customerArgumentCaptorValue = customerArgumentCaptor.getValue();

        assertThat(customerArgumentCaptorValue).isEqualToComparingFieldByField(customer);
    }

}