package com.amigoscode.testing.payment.twilio;

import com.amigoscode.testing.payment.MessageSender;
import com.twilio.sdk.type.PhoneNumber;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(
        value = "twilio.enabled",
        havingValue = "false"
)
public class MockTwilioService implements MessageSender {
    @Override
    public String sendMessage(PhoneNumber to, PhoneNumber from, String body) {
        return "Message from Mock Sent";
    }
}
