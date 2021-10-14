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

  def sendMessage(storesInStock: Seq[String]): Unit = {
    val from = new PhoneNumber(twilioConfig.twilioPhoneNumber)
    val to = new PhoneNumber(twilioConfig.outboundPhoneNumber)
    val stores = storesInStock.mkString("\n\n")
    val body = s"ITS IN STOCK BABY: ${stores}"

    Message.creator(to, from, body).create()
  }

}
