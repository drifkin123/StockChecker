import Wiring.{stockChecker, twilio}

import scala.util.{Failure, Success}

case class Product(
  name: String,
  url: String,
  cssSelector: String
)

object Main extends App {
  val url = "https://www.bestbuy.com/site/nvidia-geforce-rtx-3080-10gb-gddr6x-pci-express-4-0-graphics-card-titanium-and-black/6429440.p?skuId=6429440"
  val css = "button.add-to-cart-button"
  val SLEEP_TIME_SECONDS = 2 * 60
  val product = Product("rtx 3080", url, css)

  while (true) {
    val tryIsInStock = stockChecker.hasStock(product)

    val isInStock = tryIsInStock match {
      case Failure(exception) => {
        println("Failed checking stock")
        println(exception.printStackTrace)
        false
      }
      case Success(value) => value
    }

    if (isInStock) {
      twilio.sendMessage()
    } else {
      println("Unfortunately not :(")
    }

    println(s"Checking again in ${SLEEP_TIME_SECONDS / 60} minutes")
    Thread.sleep(SLEEP_TIME_SECONDS * 1000)
  }
}
