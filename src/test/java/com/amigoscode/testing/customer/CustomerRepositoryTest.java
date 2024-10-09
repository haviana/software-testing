package com.amigoscode.testing.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DataJpaTest(properties = {
        "spring.jpa.properties.javax.persistence.validation.mode=none"
})
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository underTest;
    @Test
    void itShouldSelectCustomerByPhoneNumber() {
        // Given
        UUID id = UUID.randomUUID();
        String phoneNumber = "0000";
        Customer customer = new Customer(id, "Abel", phoneNumber);
        // When
        underTest.save(customer);

        // Then
        Optional<Customer> savedCustomer = underTest.selectCustomerNumberByPhoneNumber(phoneNumber);
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
    void itShouldNotFindAnyCustomerWithPhoneNumber() {
        // Given
        UUID id = UUID.randomUUID();
        String phoneNumber = "0000";
        Customer customer = new Customer(id, "Abel", phoneNumber);
        // When
        underTest.save(customer);

        // Then
        Optional<Customer> savedCustomer = underTest.selectCustomerNumberByPhoneNumber("000");
        assertThat(savedCustomer)
                .isNotPresent();

    }

    @Test
    void itShouldNotSaveACustomerWithNullName() {
        // Given

        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, null,"12334");

        // When
        assertThatThrownBy(()-> underTest.save(customer))
                .hasMessageContaining("not-null property references a null or transient value : com.amigoscode.testing.customer.Customer.name")
                .isInstanceOf(DataIntegrityViolationException.class);

        // Then

    }

    @Test
    void itShouldNotSaveACustomerWithNullPhoneNumber() {
        // Given

        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "name",null);

        // When
        assertThatThrownBy(()-> underTest.save(customer))
                .hasMessageContaining("not-null property references a null or transient value : com.amigoscode.testing.customer.Customer.phoneNumber")
                .isInstanceOf(DataIntegrityViolationException.class);

        // Then

    }

    @Test
    void findCustomerByPhoneNumber() {
    }


    @Test
    void itShouldSaveACustomer() {
        // Given
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id, "name","000000");

        // When

        underTest.save(customer);
        underTest.findById(id);
        // Then

        assertThat(underTest.findById(id))
                .isPresent()
                .hasValueSatisfying(
                        c->{c.getName().equals(customer.getName());});
    }
}