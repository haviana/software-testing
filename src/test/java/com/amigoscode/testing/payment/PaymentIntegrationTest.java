package com.amigoscode.testing.payment;

import com.amigoscode.testing.customer.Customer;
import com.amigoscode.testing.customer.CustomerRegistrationRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.model.Application;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.Fail.fail;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentIntegrationTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void itShouldCreatePaymentSuccessfully() throws Exception {
        // Given
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "James", "+351926828283");

        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(customer);

        ResultActions customerRegistrationResult = mockMvc.perform(put("/api/v1/customer-registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(customerRegistrationRequest))));

        Payment payment = new Payment(1L, id, new BigDecimal("100.00"), Currency.GBP, "X0X0X0", "Zakat");
        PaymentRequest paymentRequest = new PaymentRequest(payment);


        // When
        ResultActions paymentResult = mockMvc.perform(post("/api/v1/payment/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJson(paymentRequest)));

        // Then
        paymentResult.andExpect(status().isOk());
        customerRegistrationResult.andExpect(status().isOk());

        // TODO:  DO not use paymentRepository instead use and endpoint for getting by ID
        assertThat(paymentRepository.findById(payment.getPaymentId()))
                .isPresent()
                        .hasValueSatisfying(p-> assertThat(p).isEqualToComparingFieldByField(payment));
        customerRegistrationResult.andExpect(status().isOk());
    }

    private String objectToJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            fail("Failed to convert Object to Json");
            return null;
        }
    }
}
