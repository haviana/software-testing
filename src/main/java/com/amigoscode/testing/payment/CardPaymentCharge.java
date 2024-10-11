package com.amigoscode.testing.payment;

public class CardPaymentCharge {

    public boolean isCardDebited() {
        return isCardDebited;
    }

    private final boolean isCardDebited;

    public CardPaymentCharge(boolean isCardDebited) {
        this.isCardDebited = isCardDebited;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CardPaymentCharge{");
        sb.append("isCardDebited=").append(isCardDebited);
        sb.append('}');
        return sb.toString();
    }
}
