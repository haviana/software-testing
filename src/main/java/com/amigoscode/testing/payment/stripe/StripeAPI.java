package com.amigoscode.testing.payment.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import com.stripe.param.ChargeCreateParams;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StripeAPI {

    public Charge create(ChargeCreateParams requestMap, RequestOptions options) throws StripeException {
        return Charge.create(requestMap, options);
    }
}
