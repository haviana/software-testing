package com.amigoscode.testing.payment;

import java.util.Arrays;

public enum Currency {
    USD,
    GBP,
    EUR;

    public boolean isCurrencyValid(Currency cur){
        return Arrays.stream(values()).anyMatch(c->c.equals(cur));
    }
}