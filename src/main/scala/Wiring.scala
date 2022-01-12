import Messenger.{TwilioConfig, TwilioMessenger}
import domain.{BestBuyProduct, Product}

object Wiring {
    println(sys.env)
    val env: Map[String, String] = sys.env
    val ACCOUNT_SID: String = env("ACCOUNT_SID")
    val AUTH_TOKEN: String = env("AUTH_TOKEN")
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

    val products: Seq[Product] = Seq(
        new BestBuyProduct(
            "rtx 3060 OC",
            "https://www.bestbuy.com/site/evga-nvidia-geforce-rtx-3060-xc-gaming-12gb-gddr6-pci-express-4-0-graphics-card/6454329.p?skuId=6454329",
        ),
        new BestBuyProduct(
            "rtx 3060 ti",
            "https://www.bestbuy.com/site/nvidia-geforce-rtx-3060-ti-8gb-gddr6-pci-express-4-0-graphics-card-steel-and-black/6439402.p?skuId=6439402",
        ),
        new BestBuyProduct(
            "rtx 3070 ti",
            "https://www.bestbuy.com/site/nvidia-geforce-rtx-3070-ti-8gb-gddr6x-pci-express-4-0-graphics-card-dark-platinum-and-black/6465789.p?skuId=6465789",
        ),
        new BestBuyProduct(
            "rtx 3070",
            "https://www.bestbuy.com/site/nvidia-geforce-rtx-3070-8gb-gddr6-pci-express-4-0-graphics-card-dark-platinum-and-black/6429442.p?skuId=6429442",
        ),
        new BestBuyProduct(
            "rtx 3080",
            "https://www.bestbuy.com/site/nvidia-geforce-rtx-3080-10gb-gddr6x-pci-express-4-0-graphics-card-titanium-and-black/6429440.p?skuId=6429440",
        )
    )

    val stockChecker = new StockChecker(twilioMessenger, jsoupWrapper, products)
}
