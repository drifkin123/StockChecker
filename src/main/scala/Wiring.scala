import Products.Rtx3080._

object Wiring {
  val ACCOUNT_SID: String = sys.env.getOrElse("ACCOUNT_SID", "")
  val AUTH_TOKEN: String = sys.env.getOrElse("AUTH_TOKEN", "")
  val TWILIO_PHONE_NUMBER: String = sys.env.getOrElse("TWILIO_PHONE_NUMBER", "")
  val OUTBOUND_NUMBER: String = sys.env.getOrElse("OUTBOUND_NUMBER", "")

  val twilioConfig: TwilioConfig = TwilioConfig(
    ACCOUNT_SID,
    AUTH_TOKEN,
    TWILIO_PHONE_NUMBER,
    OUTBOUND_NUMBER
  )

  val twilio = new TwilioWrapper(twilioConfig)

  val rtx3080StockChecker = new StockChecker(rtx3080)
}
