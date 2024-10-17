package com.amigoscode.testing.payment.twilio;

import com.twilio.sdk.Twilio;
import com.twilio.sdk.resource.api.v2010.account.Message;
import com.twilio.sdk.type.PhoneNumber;
import org.springframework.stereotype.Service;

@Service
public class TwilioAPI {
    // Find your Account Sid and Token at twilio.com/console
    public static final String ACCOUNT_SID = "ACb1a01dce46c9eb402812acdcfc122513";
    public static final String AUTH_TOKEN = "ed7eda44769f49d5f10894b8c856adc1";

    public TwilioAPI() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public String sendTwilioMessage(PhoneNumber to, PhoneNumber from, String body) {
        System.out.println(body);
        Message sentMessage = Message.create(ACCOUNT_SID,to,from,body).execute();
        return sentMessage.getSid();
    }
}
