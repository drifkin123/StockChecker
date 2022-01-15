import Messenger.Messenger
import domain.Product
import web.JsoupWrapper

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Random, Success, Try}

class StockChecker(messenger: Messenger, jsoupWrapper: JsoupWrapper, products: Seq[Product]) {
    var SLEEP_TIME_SECONDS: Long = 2 * 60L
    var MAX_SLEEP_TIME_BETWEEN_REQUESTS: Long = 5000L
    var MIN_SLEEP_TIME_BETWEEN_REQUESTS: Long = 2000L

    val r: Random.type = scala.util.Random

    def checkLooped(): Unit = {
        val groupedProducts = products.groupBy(_.company).toSeq.map(_._2)
        groupedProducts.foreach(innerProducts => {
            println(s"Start checking for ${innerProducts.head.company} products (${innerProducts.length} products)")
            Future {
                while (true) {
                    check(innerProducts)
                }
            }
        })

        Thread.sleep(Long.MaxValue)
    }

    def check(productsToCheck: Seq[Product]): Unit = {
        val tryIsInStock = hasStock(productsToCheck)

        tryIsInStock match {
            case Failure(exception) => {
                println(s"Failed checking stock: ${exception.getMessage}\nWaiting 5 seconds")
                Thread.sleep(5000L)
            }
            case Success(_) => ()
        }
    }

    private def hasStock(productsToCheck: Seq[Product]): Try[Unit] = Try {
        productsToCheck.foreach(product => {
            val randomTime = r.nextLong(MAX_SLEEP_TIME_BETWEEN_REQUESTS) + MIN_SLEEP_TIME_BETWEEN_REQUESTS
            Thread.sleep(randomTime)
            print(s"Checking for ${product.name}...")

            val doc = jsoupWrapper.connectAndGet(product.url)

            if (product.isInStock(doc)) {
                println(s"✔️")
                messenger.sendMessage(s"${product.name} is in stock at ${product.company}: ${product.url}")
            } else {
                println(s"❌")
            }
        })
    }
}
