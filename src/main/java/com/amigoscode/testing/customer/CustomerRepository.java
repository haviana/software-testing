package com.amigoscode.testing.customer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends CrudRepository<Customer, UUID> {
    Optional<Customer> findCustomerByPhoneNumber(String phoneNumber);

    @Query(value = "select id, name, phone_number" +
            "from customer" +
            "where phone_number=:phoneNumber",
            nativeQuery = true
    )
    Optional<Customer> selectCustomerNumberByPhoneNumber(
            @Param("phone_number")
            String phoneNumber);

}
