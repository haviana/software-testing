package com.amigoscode.testing.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository underTest;
    @Test
    void itShouldSelectCustomerByPhoneNumber() {
        // Given


        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "Abel", "0000");
        // When
        underTest.save(customer);

        // Then
        Optional<Customer> savedCustomer = underTest.findById(id);
        assertThat(savedCustomer)
                .isPresent()
                .hasValueSatisfying(
                        c->{
                            assertThat(c.getId()).isEqualTo(id);
                            assertThat(c.getName()).isEqualTo("Abel");
                            assertThat(c.getPhoneNumber()).isEqualTo("0000");
                            assertThat(c).isEqualToComparingFieldByField(customer);
                        } );
    }

    @Test
    void findCustomerByPhoneNumber() {
    }

    
}