package com.amigoscode.testing.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerRegistrationService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerRegistrationService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void registerNewCustomer(CustomerRegistrationRequest request) {
        // 1. PhoneNumber is taken

        Optional<Customer> customerByPhoneNumber = customerRepository.findCustomerByPhoneNumber(request.getCustomer().getPhoneNumber());
        // 2. If taken lets check if it belongs to the same customer
        // 2.1 Returns the customer
        // 2.2. Thrown an exception
        if (customerByPhoneNumber.isPresent()) {

            if ( customerByPhoneNumber.get().getId().equals(request.getCustomer().getId())
                    && customerByPhoneNumber.get().getName().equals(request.getCustomer().getName())){
                    return;
            }
            throw new DuplicateResourceException(String.format(" Duplicate customer : %s ", customerByPhoneNumber.get()));
        }

        // 3. Save customer
        if(request.getCustomer().getId() == null) {
            request.getCustomer().setId(UUID.randomUUID());
        }

        customerRepository.save(request.getCustomer());
    }
}
