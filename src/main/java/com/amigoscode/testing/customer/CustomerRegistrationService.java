package com.amigoscode.testing.customer;

import com.amigoscode.testing.utils.PhoneNumberValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerRegistrationService {

    private final CustomerRepository customerRepository;
    private final PhoneNumberValidator phoneNumberValidator;

    @Autowired
    public CustomerRegistrationService(CustomerRepository customerRepository,
                                       PhoneNumberValidator phoneNumberValidator) {
        this.customerRepository = customerRepository;
        this.phoneNumberValidator = phoneNumberValidator;
    }


    public void registerNewCustomer(CustomerRegistrationRequest request) {

        String phoneNumber = request.getCustomer().getPhoneNumber();
        if (!phoneNumberValidator.test(phoneNumber)){
            throw new IllegalStateException("Phone Number " +  phoneNumber + " is not valid");
        }

        Optional<Customer> customerByPhoneNumber = customerRepository.selectCustomerNumberByPhoneNumber(request.getCustomer().getPhoneNumber());
        
        if (customerByPhoneNumber.isPresent()) {

            if ( customerByPhoneNumber.get().getId().equals(request.getCustomer().getId())
                    && customerByPhoneNumber.get().getName().equals(request.getCustomer().getName())){
                    return;
            }
            throw new DuplicateResourceException(String.format(" Duplicate customer : %s ", customerByPhoneNumber.get().getId()));
        }

        if(request.getCustomer().getId() == null) {
            request.getCustomer().setId(UUID.randomUUID());
        }

        customerRepository.save(request.getCustomer());
    }
}
