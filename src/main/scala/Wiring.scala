import domain.{Product, ProductLocation}

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

  val bestBuyRtx3080: ProductLocation = ProductLocation(
    "bestBuy",
    "https://www.bestbuy.com/site/nvidia-geforce-rtx-3080-10gb-gddr6x-pci-express-4-0-graphics-card-titanium-and-black/6429440.p?skuId=6429440",
    "button.add-to-cart-button",
    "Add to Cart"
  )

  val newEggRtx3080: ProductLocation = ProductLocation(
    "newEgg",
    "https://www.newegg.com/msi-geforce-rtx-3080-rtx-3080-ventus-3x-10g/p/N82E16814137600?Item=N82E16814137600&quicklink=true",
    "#ProductBuy button.btn-wide",
    "Add to cart "
  )

  val rtx3080: Product = Product(
    "rtx3080",
    Seq(bestBuyRtx3080, newEggRtx3080)
  )

  val rtx3080StockChecker = new StockChecker(rtx3080)
}
