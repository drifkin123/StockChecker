import Messenger.Messenger
import domain.Product
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import scala.util.{Failure, Random, Success, Try}

trait JsoupWrapper {
    def connectAndGet(url: String): Document
}

class JsoupWrapperImpl() extends JsoupWrapper {
    override def connectAndGet(url: String): Document = {
        Jsoup
            .connect(url)
            .get()
    }
}

class StockChecker(messenger: Messenger, jsoupWrapper: JsoupWrapper, products: Seq[Product]) {
    var SLEEP_TIME_SECONDS: Long = 2 * 60L
    var MAX_SLEEP_TIME_BETWEEN_REQUESTS: Long = 5000L
    var MIN_SLEEP_TIME_BETWEEN_REQUESTS: Long = 1000L

    val r: Random.type = scala.util.Random

    def checkLooped(): Unit = {
        while (true) {
            check()
        }
    }

    def check(): Unit = {
        println("Checking...")
        val tryIsInStock = hasStock

        tryIsInStock match {
            case Failure(exception) => println(s"Failed checking stock: ${exception.getMessage}")
            case Success(maybeMessage) => maybeMessage.foreach(messenger.sendMessage)
        }
    }

    private def hasStock: Try[Option[String]] = Try {

        val productsInStock: Seq[Product] = products.flatMap(product => {
            val doc: Document = jsoupWrapper.connectAndGet(product.url)

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
