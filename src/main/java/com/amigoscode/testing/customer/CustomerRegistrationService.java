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

        Optional<Customer> customerByPhoneNumber = customerRepository.findCustomerByPhoneNumber(request.getCustomer().getPhoneNumber());
        if (customerByPhoneNumber.isPresent()) {

            if ( customerByPhoneNumber.get().getId().equals(request.getCustomer().getId())
                    && customerByPhoneNumber.get().getName().equals(request.getCustomer().getName())){
                    return;
            }
            throw new DuplicateResourceException(String.format(" Duplicate customer : %s ", customerByPhoneNumber.get()));
        }

        if(request.getCustomer().getId() == null) {
            request.getCustomer().setId(UUID.randomUUID());
        }

        customerRepository.save(request.getCustomer());
    }
}
