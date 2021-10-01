import Wiring.{stockChecker, twilio}

object Main extends App {
  val URL_TO_CHECK = "https://www.bestbuy.com/site/nvidia-geforce-rtx-3080-10gb-gddr6x-pci-express-4-0-graphics-card-titanium-and-black/6429440.p?skuId=6429440"

  while (true) {
    val isInStock = stockChecker.hasStock(URL_TO_CHECK, "button.add-to-cart-button")

    if (isInStock) {
      twilio.sendMessage()
    } else {
      println("Unfortunately not :(")
    }

    Thread.sleep(5 * 1000)
  }
}
