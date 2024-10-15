package com.amigoscode.testing.payment.stripe;

import com.amigoscode.testing.payment.CardPaymentCharge;
import com.amigoscode.testing.payment.CardPaymentCharger;
import com.amigoscode.testing.payment.Currency;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import com.stripe.param.ChargeCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@ConditionalOnProperty(
        value = "stripe.enabled",
        havingValue = "true"
)
public class StripeService implements CardPaymentCharger {

    private final StripeAPI stripeAPI;
    private final RequestOptions requestOptions = RequestOptions.builder()
            .setApiKey("sk_test_QUXcoU3BnbZXp6IMVi7BkW8s")
            .build();

    @Autowired
    public StripeService(StripeAPI stripeAPI) {
        this.stripeAPI = stripeAPI;
    }

    @Override
    public CardPaymentCharge chargeCard(String cardSource, BigDecimal amount, Currency currency, String description) {

        ChargeCreateParams params =
                ChargeCreateParams.builder()
                        .setAmount(amount.longValue())
                        .setCurrency(currency.toString())
                        .setSource(description)
                        .build();

        try {
            Charge charge = stripeAPI.create(params, requestOptions);
            return new CardPaymentCharge(charge.getPaid());
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }

    }
}
