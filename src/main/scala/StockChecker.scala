import Messenger.Messenger
import domain.Product
import web.JsoupWrapper

import scala.util.{Failure, Random, Success, Try}

class StockChecker(messenger: Messenger, jsoupWrapper: JsoupWrapper, products: Seq[Product]) {
    var SLEEP_TIME_SECONDS: Long = 2 * 60L
    var MAX_SLEEP_TIME_BETWEEN_REQUESTS: Long = 5000L
    var MIN_SLEEP_TIME_BETWEEN_REQUESTS: Long = 2000L

    val r: Random.type = scala.util.Random

    def checkLooped(): Unit = {
        while (true) {
            check()
        }
    }

    def check(): Unit = {
        val tryIsInStock = hasStock

        tryIsInStock match {
            case Failure(exception) => println(s"Failed checking stock: ${exception.getMessage}")
            case Success(maybeMessage) => maybeMessage.foreach(messenger.sendMessage)
        }

        println("-------------------------------")
    }

    private def hasStock: Try[Option[String]] = Try {

        val productsInStock: Seq[Product] = products.flatMap(product => {
            println(s"Checking for ${product.name}...")

            val doc = jsoupWrapper.connectAndGet(product.url)

            val randomTime = r.nextLong(MAX_SLEEP_TIME_BETWEEN_REQUESTS) + MIN_SLEEP_TIME_BETWEEN_REQUESTS
            Thread.sleep(randomTime)

            if (product.isInStock(doc)) Some(product) else None
        })


        if (productsInStock.nonEmpty) {
            val productNamesInStock = productsInStock
                .map(_.name)
                .mkString(" and ") + s" ${if (productsInStock.length > 1) "are" else "is"} in stock!"

            val urlsInStock = productsInStock.map(product => s"\n\n${product.url}").mkString("")

            Some(productNamesInStock + urlsInStock)
        } else {
            None
        }
    }
}
