import com.twilio.Twilio
import com.twilio.`type`.PhoneNumber
import com.twilio.rest.api.v2010.account.Message

case class TwilioConfig(
  accountSid: String,
  autoToken: String,
  twilioPhoneNumber: String,
  outboundPhoneNumber: String
)

class TwilioWrapper(
  twilioConfig: TwilioConfig
) {
  Twilio.init(twilioConfig.accountSid, twilioConfig.autoToken)

  def sendMessage(): Unit = {
    val from = new PhoneNumber(twilioConfig.twilioPhoneNumber)
    val to = new PhoneNumber(twilioConfig.outboundPhoneNumber)
    val body = "BUY THE GRAPHICS CARD RIGHT NOW"

    Message.creator(to, from, body).create()
  }

}
