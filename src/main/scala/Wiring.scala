import Messenger.{TwilioConfig, TwilioMessenger}
import domain.Product
import web.JsoupWrapperImpl

object Wiring {
    println(sys.env)
    val env: Map[String, String] = sys.env
    val ACCOUNT_SID: String = env("ACCOUNT_SID")
    val AUTH_TOKEN: String = env("AUTH_TOKEN")
    val PATH_TO_PRODUCTS: String = env("PATH_TO_PRODUCTS")
    val TWILIO_PHONE_NUMBER = "+19412365176"
    val OUTBOUND_NUMBER = "+18186655087"

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
