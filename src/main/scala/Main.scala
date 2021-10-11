import Wiring.{rtx3080StockChecker, twilio}

import scala.util.{Failure, Success}

object Main extends App {
  val SLEEP_TIME_SECONDS = 2 * 60

  while (true) {
    val tryIsInStock = rtx3080StockChecker.hasStock()

    val storesInStock: Seq[String] = tryIsInStock match {
      case Failure(exception) => {
        println("Failed checking stock")
        println(exception.printStackTrace())
        Seq()
      }
      case Success(value) => value
    }

    if (storesInStock.nonEmpty) {
      twilio.sendMessage(storesInStock)
    } else {
      println("Unfortunately not :(")
    }

    println(s"Checking again in ${SLEEP_TIME_SECONDS / 60} minutes")
    Thread.sleep(SLEEP_TIME_SECONDS * 1000)
  }
}
