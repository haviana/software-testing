package com.amigoscode.testing.payment;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentRequest {

    private final Payment payment;

    public PaymentRequest(@JsonProperty("payment") Payment payment) {
        this.payment = payment;
    }

    public Payment getPayment() {
        return payment;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PaymentRequest{");
        sb.append("payment=").append(payment);
        sb.append('}');
        return sb.toString();
    }
}
