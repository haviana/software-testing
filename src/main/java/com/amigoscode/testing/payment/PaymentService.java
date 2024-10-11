package com.amigoscode.testing.payment;

import com.amigoscode.testing.customer.Customer;
import com.amigoscode.testing.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {
    private static final List<Currency> ACCEPTED_CURRENCIES = List.of(Currency.USD, Currency.GBP);


    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;
    private final CardPaymentCharger cardPaymentCharger;



    @Autowired
    public PaymentService(CustomerRepository customerRepository,
                          PaymentRepository paymentRepository,
                          CardPaymentCharger paymentCharger) {
        this.customerRepository = customerRepository;
        this.paymentRepository = paymentRepository;
        this.cardPaymentCharger = paymentCharger;
    }

    void chargeCard(UUID customerID, PaymentRequest paymentRequest){
        // 1. Does customer exists if not thro
        Optional<Customer> customer = customerRepository.findById(customerID);

        if (!customer.isPresent()){
            throw new IllegalStateException(String.format("Customer %s already exists",customerID));
        }

        // 2. Do we support the currency if not throw
        if (!ACCEPTED_CURRENCIES.contains(paymentRequest.getPayment().getCurrency())){
            String message = String.format(
                    "Currency [%s] not supported",
                    paymentRequest.getPayment().getCurrency());
            throw new IllegalStateException(message);
        }
        // 3. Charge card
        CardPaymentCharge cardPaymentCharge = cardPaymentCharger.chargeCard(
                paymentRequest.getPayment().getSource(),
                paymentRequest.getPayment().getAmount(),
                paymentRequest.getPayment().getCurrency(),
                paymentRequest.getPayment().getDescription()
        );
        // 4. If not debited throw
        if (!cardPaymentCharge.isCardDebited()) {
            throw new IllegalStateException(String.format("Card not debited for customer %s", customerID));
        }
        // 5. Insert Payment

        paymentRequest.getPayment().setCustomerId(customerID);

        paymentRepository.save(paymentRequest.getPayment());
        // 6. TODO: send sms

    }
}
