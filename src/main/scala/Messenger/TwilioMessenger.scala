package Messenger

import com.twilio.Twilio
import com.twilio.`type`.PhoneNumber
import com.twilio.rest.api.v2010.account.Message

case class TwilioConfig(
    accountSid: String,
    autoToken: String,
    twilioPhoneNumber: String,
    outboundPhoneNumber: String
)

class TwilioMessenger(twilioConfig: TwilioConfig) extends Messenger {

    Twilio.init(twilioConfig.accountSid, twilioConfig.autoToken)

    override def sendMessage(message: String): Unit = {
        val from = new PhoneNumber(twilioConfig.twilioPhoneNumber)
        val to = new PhoneNumber(twilioConfig.outboundPhoneNumber)

        Message.creator(to, from, message).create()
    }
}
