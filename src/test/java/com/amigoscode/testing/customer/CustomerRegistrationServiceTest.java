package com.amigoscode.testing.customer;

import com.amigoscode.testing.utils.PhoneNumberValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;

class CustomerRegistrationServiceTest {

    private CustomerRegistrationService underTest;

    @Captor
    private ArgumentCaptor<Customer> customerArgumentCaptor;
    @Mock
    private  CustomerRepository customerRepository;

    @Mock
    private PhoneNumberValidator phoneNumberValidator;

    @BeforeEach
    void setup(){
        MockitoAnnotations.initMocks(this);
        underTest = new CustomerRegistrationService(customerRepository,phoneNumberValidator) {
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
        given(phoneNumberValidator.test(any())).willReturn(true);

        // When
        underTest.registerNewCustomer(request);

        // Then

        then(customerRepository).should().save(customerArgumentCaptor.capture());
        Customer customerArgumentCaptorValue = customerArgumentCaptor.getValue();

        assertThat(customerArgumentCaptorValue).isEqualToComparingFieldByField(customer);
    }

    @Test
    void itShouldNotSaveCustomerWhenCustomerExists() {
        // Given
        String phoneNumber = "00000";
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "Maryan", phoneNumber);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        given(customerRepository.selectCustomerNumberByPhoneNumber(phoneNumber))
                .willReturn(Optional.of(customer));

        given(phoneNumberValidator.test(any())).willReturn(true);
        // When
        underTest.registerNewCustomer(request);
        // Then
        then(customerRepository).should().selectCustomerNumberByPhoneNumber(phoneNumber);
        then(customerRepository).shouldHaveNoMoreInteractions();
        then(customerRepository).should(never()).save(any());
    }

    @Test
    void itShouldThownsAndExceptionWhenSavingaCustomer() {
        // Given
        String phoneNumber = "00000";
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "Maryan", phoneNumber);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        given(customerRepository.selectCustomerNumberByPhoneNumber(phoneNumber))
                .willReturn(Optional.of(new Customer(id, "New Customer", phoneNumber)));
        given(phoneNumberValidator.test(any())).willReturn(true);
        // When
        assertThatExceptionOfType(DuplicateResourceException.class)
                .isThrownBy(()-> { underTest.registerNewCustomer(request);})
                .withMessageStartingWith(String.format(" Duplicate customer : %s", id));
        // Then

        then(customerRepository).should(never()).save(any());

    }

    @Test
    void itShouldGenerateANewIdWhenCustomerIDIsNotValid() {
        // Given
        String phoneNumber = "00000";
        Customer customer = new Customer(null, "Maryan", phoneNumber);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        given(customerRepository.selectCustomerNumberByPhoneNumber(phoneNumber))
                .willReturn(Optional.empty());
        given(phoneNumberValidator.test(any())).willReturn(true);
        // When

        underTest.registerNewCustomer(request);
        // Then

        then(customerRepository).should().save(customerArgumentCaptor.capture());
        Customer customerArgumentCaptureValue =  customerArgumentCaptor.getValue();

        assertThat(customerArgumentCaptureValue)
                .isEqualToIgnoringGivenFields(customer,"id");
        assertThat(customerArgumentCaptureValue.getId()).isNotNull();
    }

    @Test
    void itShouldNotSaveNewCustomerWhenPhoneNumberIsInvalid() {
        // Given
        String phoneNumber = "00000";
        Customer customer = new Customer(null, "Maryan", phoneNumber);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);

        given(phoneNumberValidator.test(phoneNumber)).willReturn(false);

        // When
        assertThatThrownBy(()-> underTest.registerNewCustomer(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Phone Number " +  phoneNumber + " is not valid");

        // Then
        then(customerRepository).shouldHaveNoInteractions();
    }
}