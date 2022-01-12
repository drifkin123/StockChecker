import Messenger.Messenger
import domain.BestBuyProduct
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.io.{BufferedSource, Source}

class StockCheckerTest extends AnyFunSuite with Matchers {

    def createFixture(itemsInStock: Map[String, Boolean], throwException: Boolean = false) = {
        new {
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

            val fakeJsoup = new FakeJsoup(createUrlToDocument(), throwException)

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
        val fixture = createFixture(itemsInStock)

        val expectedMessage = "stuff2 is in stock!\n\nhttps://www.example.com/some/other/stuff2"

        // when
        fixture.stockChecker.check()

        // then
        fixture.fakeMessenger.messageSent shouldBe expectedMessage
    }

    test("Send alert with correct message when multiple products are in stock") {
        // given
        val itemsInStock = Map(
            "stuff1" -> true,
            "stuff2" -> true
        )

        val fixture = createFixture(itemsInStock)
        val expectedMessage = "stuff1 and stuff2 are in stock!\n\nhttps://www.example.com/some/other/stuff1" +
            "\n\nhttps://www.example.com/some/other/stuff2"

        // when
        fixture.stockChecker.check()

        // then
        fixture.fakeMessenger.messageSent shouldBe expectedMessage
    }

    test("Do not send alert if items are not in stock") {
        // given
        val itemsInStock = Map(
            "stuff1" -> false,
            "stuff2" -> false
        )

        val fixture = createFixture(itemsInStock)
        val expectedMessage = ""

        // when
        fixture.stockChecker.check()

        // then
        fixture.fakeMessenger.messageSent shouldBe expectedMessage
    }

    test("Catch exception if failure to get document") {
        // given
        val itemsInStock = Map(
            "stuff1" -> false,
            "stuff2" -> false
        )

        val fixture = createFixture(itemsInStock, throwException = true)

        // when
        fixture.stockChecker.check()

        // then
        fixture.fakeMessenger.messageSent shouldBe ""
    }

    // TODO: Implement before passing down to stock checker
    test("Based on domain, determine which product objects to create") {
        // Have product page with list of urls (only best buy and newegg) to check
        // if in stock
    }

    // TODO: Read url's from text file to scrape
    test("Fail with message to user to add page with urls if not there or empty") {

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
