package com.amigoscode.testing.payment;

import com.twilio.sdk.type.PhoneNumber;

public interface MessageSender {
    String sendMessage(PhoneNumber to, PhoneNumber from, String body);
}
