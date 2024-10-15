package com.amigoscode.testing.payment.stripe;


import com.amigoscode.testing.payment.CardPaymentCharge;
import com.amigoscode.testing.payment.Currency;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import com.stripe.param.ChargeCreateParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

class StripeServiceTest {

    private StripeService underTest;

    @Mock
    private StripeAPI stripeAPI;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new StripeService(stripeAPI);
    }

    @Test
    void itShouldChargeCard() throws StripeException {
        // Given
        String cardSource = "0x0x0x";
        BigDecimal amount = new BigDecimal("10.00");
        String description = "description";

        Charge t = new Charge();
        t.setPaid(true);
        given(stripeAPI.create(any(),any())).willReturn(t);

        // When


        CardPaymentCharge cardPaymentCharge = underTest.chargeCard(cardSource, amount, Currency.USD, description);

        // Then

        ArgumentCaptor<ChargeCreateParams> paramsArgumentCaptor = ArgumentCaptor.forClass(ChargeCreateParams.class);
        ArgumentCaptor<RequestOptions> optionsArgumentCaptor = ArgumentCaptor.forClass(RequestOptions.class);

        then(stripeAPI).should().create(paramsArgumentCaptor.capture(),optionsArgumentCaptor.capture());

        assertThat(paramsArgumentCaptor.getValue().getAmount().equals(amount));
        assertThat(paramsArgumentCaptor.getValue().getCurrency().equals(Currency.USD));

        assertThat(optionsArgumentCaptor.getValue()).isNotNull();

        assertThat(cardPaymentCharge.isCardDebited()).isTrue();


    }
}