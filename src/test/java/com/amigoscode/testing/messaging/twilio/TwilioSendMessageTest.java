package com.amigoscode.testing.messaging.twilio;

import com.amigoscode.testing.payment.twilio.TwilioAPI;
import com.amigoscode.testing.payment.twilio.TwilioService;
import com.twilio.sdk.type.PhoneNumber;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


class TwilioSendMessageTest {

    private TwilioService underTest;

     @Mock
     private TwilioAPI twilioAPI;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new TwilioService(twilioAPI);
    }

    @Test
    void itShouldSendAMessage(){
        String body = "send message with twilio";
        PhoneNumber from = new PhoneNumber("+351926828283");
        PhoneNumber to = new PhoneNumber("+351965493567");

        given(twilioAPI.sendTwilioMessage(any(),any(),any())).willReturn(body);

        ArgumentCaptor<PhoneNumber> fromArgumentCaptor = ArgumentCaptor.forClass(PhoneNumber.class);
        ArgumentCaptor<PhoneNumber> toArgumentCaptor = ArgumentCaptor.forClass(PhoneNumber.class);
        ArgumentCaptor<String> bodyArgumentCaptor = ArgumentCaptor.forClass(String.class);


        String message = underTest.sendMessage(to, from, body);

        then(twilioAPI).should().sendTwilioMessage(fromArgumentCaptor.capture(),toArgumentCaptor.capture(),bodyArgumentCaptor.capture());

        assertThat(message).isEqualTo(body);

        Assertions.assertThat(fromArgumentCaptor.getValue().equals(from));
        Assertions.assertThat(toArgumentCaptor.getValue().equals(to));
        Assertions.assertThat(bodyArgumentCaptor.getValue().equals(body));

    }
}
