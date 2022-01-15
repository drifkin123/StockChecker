import Messenger.Messenger
import domain.{BestBuyProduct, EvgaProduct, NeweggProduct, Product}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import web.JsoupWrapper

import scala.io.{BufferedSource, Source}

class StockCheckerTest extends AnyFunSuite with Matchers {

    def createFixture(products: Seq[Product], urlToDocumentMap: Map[String, Document], throwException: Boolean = false) = {
        new {
            val fakeJsoup = new FakeJsoup(urlToDocumentMap, throwException)

            val fakeMessenger = new FakeMessenger
            val stockChecker = new StockChecker(fakeMessenger, fakeJsoup, products)
            stockChecker.MAX_SLEEP_TIME_BETWEEN_REQUESTS = 100L
            stockChecker.MIN_SLEEP_TIME_BETWEEN_REQUESTS = 100L
        }
    }

    test("Send alert with correct message when product is in stock") {
        // given
        val itemsInStock = Map(
            "stuff1" -> false,
            "stuff2" -> true
        )

        def getDocumentForStockStatus(inStock: Boolean): Document = {
            if (inStock) createDocumentFromHtml("/documents/bestBuyInStock.html") else createDocumentFromHtml("/documents/bestBuyOutOfStock.html")
        }

        def createUrlToDocument(): Map[String, Document] = Map(
            "https://www.example.com/some/other/stuff1" -> getDocumentForStockStatus(itemsInStock("stuff1")),
            "https://www.example.com/some/other/stuff2" -> getDocumentForStockStatus(itemsInStock("stuff2"))
        )

        val products = Seq(
            new BestBuyProduct("stuff1", "https://www.example.com/some/other/stuff1"),
            new BestBuyProduct("stuff2", "https://www.example.com/some/other/stuff2")
        )

        val fixture = createFixture(products, createUrlToDocument())

        val expectedMessage = "stuff2 is in stock at bestbuy: https://www.example.com/some/other/stuff2"

        // when
        fixture.stockChecker.check(products)

        // then
        fixture.fakeMessenger.messageSent shouldBe expectedMessage
    }

    test("Send alert with correct message when multiple products are in stock") {
        // given
        val itemsInStock = Map(
            "stuff1" -> true,
            "stuff2" -> true
        )

        def getDocumentForStockStatus(inStock: Boolean): Document = {
            if (inStock) createDocumentFromHtml("/documents/bestBuyInStock.html") else createDocumentFromHtml("/documents/bestBuyOutOfStock.html")
        }

        def createUrlToDocument(): Map[String, Document] = Map(
            "https://www.example.com/some/other/stuff1" -> getDocumentForStockStatus(itemsInStock("stuff1")),
            "https://www.example.com/some/other/stuff2" -> getDocumentForStockStatus(itemsInStock("stuff2"))
        )

        val products = Seq(
            new BestBuyProduct("stuff1", "https://www.example.com/some/other/stuff1"),
            new BestBuyProduct("stuff2", "https://www.example.com/some/other/stuff2")
        )

        val fixture = createFixture(products, createUrlToDocument())
        val expectedMessage = "stuff1 is in stock at bestbuy: https://www.example.com/some/other/stuff1" +
          "stuff2 is in stock at bestbuy: https://www.example.com/some/other/stuff2"

        // when
        fixture.stockChecker.check(products)

        // then
        fixture.fakeMessenger.messageSent shouldBe expectedMessage
    }

    test("Do not send alert if items are not in stock") {
        // given
        val itemsInStock = Map(
            "stuff1" -> false,
            "stuff2" -> false
        )

        def getDocumentForStockStatus(inStock: Boolean): Document = {
            if (inStock) createDocumentFromHtml("/documents/bestBuyInStock.html") else createDocumentFromHtml("/documents/bestBuyOutOfStock.html")
        }

        def createUrlToDocument(): Map[String, Document] = Map(
            "https://www.example.com/some/other/stuff1" -> getDocumentForStockStatus(itemsInStock("stuff1")),
            "https://www.example.com/some/other/stuff2" -> getDocumentForStockStatus(itemsInStock("stuff2"))
        )

        val products = Seq(
            new BestBuyProduct("stuff1", "https://www.example.com/some/other/stuff1"),
            new BestBuyProduct("stuff2", "https://www.example.com/some/other/stuff2")
        )

        val fixture = createFixture(products, createUrlToDocument())
        val expectedMessage = ""

        // when
        fixture.stockChecker.check(products)

        // then
        fixture.fakeMessenger.messageSent shouldBe expectedMessage
    }

    test("Catch exception if failure to get document") {
        // given
        val itemsInStock = Map(
            "stuff1" -> false,
            "stuff2" -> false
        )

        def getDocumentForStockStatus(inStock: Boolean): Document = {
            if (inStock) createDocumentFromHtml("/documents/bestBuyInStock.html") else createDocumentFromHtml("/documents/bestBuyOutOfStock.html")
        }

        def createUrlToDocument(): Map[String, Document] = Map(
            "https://www.example.com/some/other/stuff1" -> getDocumentForStockStatus(itemsInStock("stuff1")),
            "https://www.example.com/some/other/stuff2" -> getDocumentForStockStatus(itemsInStock("stuff2"))
        )

        val products = Seq(
            new BestBuyProduct("stuff1", "https://www.example.com/some/other/stuff1"),
            new BestBuyProduct("stuff2", "https://www.example.com/some/other/stuff2")
        )

        val fixture = createFixture(products, createUrlToDocument(), throwException = true)

        // when
        fixture.stockChecker.check(products)

        // then
        fixture.fakeMessenger.messageSent shouldBe ""
    }

    test("Check newegg stock") {
        // given
        val neweggUrlInStock = "https://newegg.com/stuffInStock"
        val neweggUrlNotInStock = "https://newegg.com/stuffNotInStock"

        val neweggDocInStock = createDocumentFromHtml("/documents/neweggInStock.html")
        val neweggDocNotInStock = createDocumentFromHtml("/documents/neweggOutOfStock.html")

        val urlToDocumentMap = Map(
            neweggUrlInStock -> neweggDocInStock,
            neweggUrlNotInStock -> neweggDocNotInStock
        )

        val products: Seq[Product] = Seq(
            new NeweggProduct("stuffInStock", neweggUrlInStock),
            new NeweggProduct("stuffNotInStock", neweggUrlNotInStock)
        )

        val fixture = createFixture(products, urlToDocumentMap)

        // when
        fixture.stockChecker.check(products)

        // then
        fixture.fakeMessenger.messageSent shouldBe s"stuffInStock is in stock at newegg: ${neweggUrlInStock}"
    }

    test("Check evga stock") {
        // given
        val evgaUrlInStock = "https://evga.com/stuffInStock"
        val evgaUrlNotInStock = "https://evga.com/stuffNotInStock"

        val evgaDocInStock = createDocumentFromHtml("/documents/evgaInStock.html")
        val evgaDocNotInStock = createDocumentFromHtml("/documents/evgaOutOfStock.html")

        val urlToDocumentMap = Map(
            evgaUrlInStock -> evgaDocInStock,
            evgaUrlNotInStock -> evgaDocNotInStock
        )

        val products: Seq[Product] = Seq(
            new EvgaProduct("stuffInStock", evgaUrlInStock),
            new EvgaProduct("stuffNotInStock", evgaUrlNotInStock)
        )

        val fixture = createFixture(products, urlToDocumentMap)

        // when
        fixture.stockChecker.check(products)

        // then
        fixture.fakeMessenger.messageSent shouldBe s"stuffInStock is in stock at evga: ${evgaUrlInStock}"
    }

    class FakeMessenger extends Messenger {
        var messageSent = ""

        override def sendMessage(message: String): Unit = {
            messageSent += message
        }
    }

    private def createDocumentFromHtml(file: String): Document = {
        val source: BufferedSource = Source.fromURL(getClass.getResource(file))

        val htmlString = new StringBuilder("")
        for (line <- source.getLines) {
            htmlString ++= line
        }
        source.close()

        Jsoup.parse(htmlString.toString())
    }

    class FakeJsoup(urlToHtml: Map[String, Document], throwException: Boolean = false) extends JsoupWrapper {
        override def connectAndGet(url: String): Document = {
            if (throwException) throw new RuntimeException("Meep mop")
            urlToHtml(url)
        }
    }

}
