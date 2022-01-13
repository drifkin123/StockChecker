import Messenger.{TwilioConfig, TwilioMessenger}
import domain.Product
import web.JsoupWrapperImpl

object Wiring {
    val env: Map[String, String] = sys.env

    def throwException(message: String) = throw new RuntimeException(message)

    val ACCOUNT_SID: String = env.getOrElse("ACCOUNT_SID", throwException("Must set ACCOUNT_SID from your twilio account in env. See readme for further instructions"))
    val AUTH_TOKEN: String = env.getOrElse("AUTH_TOKEN", throwException("Must set AUTH_TOKEN from your twilio account in env. See readme for further instructions"))
    val PATH_TO_PRODUCTS: String = env.getOrElse("PATH_TO_PRODUCTS", throwException("Must set PATH_TO_PRODUCTS in env. See readme for further instructions"))
    val TWILIO_PHONE_NUMBER: String = env.getOrElse("TWILIO_PHONE", throwException("Must enter TWILIO_PHONE from twilio account in env. See readme for further instructions"))
    val OUTBOUND_NUMBER: String = env.getOrElse("OUTBOUND_PHONE", throwException("Must enter OUTBOUND_PHONE in env. See readme for further instructions"))

    val twilioConfig: TwilioConfig = TwilioConfig(
        ACCOUNT_SID,
        AUTH_TOKEN,
        TWILIO_PHONE_NUMBER,
        OUTBOUND_NUMBER
    )

    println(s"Config: ${twilioConfig}")

    val twilioMessenger = new TwilioMessenger(twilioConfig)

    val jsoupWrapper = new JsoupWrapperImpl

    val products: Seq[Product] = ProductGenerator.generateProducts(PATH_TO_PRODUCTS)

    val stockChecker = new StockChecker(twilioMessenger, jsoupWrapper, products)
}


