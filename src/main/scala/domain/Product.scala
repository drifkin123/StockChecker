package domain

import org.jsoup.nodes.Document
import org.jsoup.select.Elements

trait Product {
    val name: String
    val url: String
    val company: String

    def isInStock(doc: Document): Boolean

    def equals(obj: Product): Boolean =
        name == obj.name && url == obj.url

}

class BestBuyProduct(
  val name: String,
  val url: String,
  val company: String = "bestbuy"
) extends Product {
    val cssSelector: String = "button.add-to-cart-button"

    override def isInStock(doc: Document): Boolean = {
        val buttons: Elements = doc.select(cssSelector)
        val buttonTextValue = if (buttons.size() > 0) {
            buttons.first().text().trim.toLowerCase()
        } else {
            ""
        }
        print(s"(${buttonTextValue})")

        buttonTextValue == "add to cart" || buttonTextValue != "sold out"
    }
}

class NeweggProduct(
  val name: String,
  val url: String,
  val company: String = "newegg"
) extends Product {
    override def isInStock(doc: Document): Boolean = {
        val stockElements = doc.select("#ProductBuy button.btn-primary")
        val stockText = if (stockElements.size() > 0) stockElements.first().text() else ""
        "add to cart" == stockText.trim.toLowerCase
    }
}

class EvgaProduct(
  val name: String,
  val url: String,
  val company: String = "evga"
) extends Product {
    override def isInStock(doc: Document): Boolean = {
        val stockElements = doc.select("#LFrame_btnAddToCart span")
        val stockText = if (stockElements.size() > 0) stockElements.first().text() else ""
        "add to cart" == stockText.trim.toLowerCase
    }
}
