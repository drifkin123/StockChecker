import Wiring.{stockChecker, twilio}

case class Product(
  name: String,
  url: String,
  cssSelector: String
)

object Main extends App {
  val url = "https://www.bestbuy.com/site/nvidia-geforce-rtx-3080-10gb-gddr6x-pci-express-4-0-graphics-card-titanium-and-black/6429440.p?skuId=6429440"
  val css = "button.add-to-cart-button"
  val SLEEP_TIME_SECONDS = 60
  val product = Product("rtx 3080", url, css)

  while (true) {
    val isInStock = stockChecker.hasStock(product)

    if (isInStock) {
      twilio.sendMessage()
    } else {
      println("Unfortunately not :(")
    }

    println(s"Checking again in ${SLEEP_TIME_SECONDS} seconds")
    Thread.sleep(SLEEP_TIME_SECONDS * 1000)
  }
}
