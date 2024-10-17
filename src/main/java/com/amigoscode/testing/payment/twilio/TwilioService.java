package com.amigoscode.testing.payment.twilio;

import com.amigoscode.testing.payment.MessageSender;
import com.twilio.sdk.resource.api.v2010.account.Message;
import com.twilio.sdk.type.PhoneNumber;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(
        value = "twilio.enabled",
        havingValue = "true"
)
public class TwilioService implements MessageSender {

    TwilioAPI twilioMessageService;

    public TwilioService(TwilioAPI twilioMessageService) {
        this.twilioMessageService = twilioMessageService;
    }

    @Override
    public String sendMessage(PhoneNumber to, PhoneNumber from, String body) {
        return twilioMessageService.sendTwilioMessage(to,from,body);
    }
}
