object Wiring {
  val ACCOUNT_SID = "ACb7471bd44057d5a044a05ae943ebc7a5"
  val AUTH_TOKEN = "7b49604d801c8ee1e38505c33745a2b2"
  val TWILIO_PHONE_NUMBER = "+12704798203"
  val OUTBOUND_NUMBER = "+18186655087"

  val twilioConfig: TwilioConfig = TwilioConfig(
    ACCOUNT_SID,
    AUTH_TOKEN,
    TWILIO_PHONE_NUMBER,
    OUTBOUND_NUMBER
  )

  val twilio = new TwilioWrapper(twilioConfig)
  val stockChecker = new StockChecker()
}
