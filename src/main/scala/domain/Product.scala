package domain

import org.jsoup.nodes.Document
import org.jsoup.select.Elements

trait Product {
    val name: String
    val url: String

    def isInStock(doc: Document): Boolean

    def equals(obj: Product): Boolean =
        name == obj.name && url == obj.url

}

class BestBuyProduct(
  val name: String,
  val url: String
) extends Product {
    val cssSelector: String = "button.add-to-cart-button"
    val textComparator: String = "Add to Cart"

    override def isInStock(doc: Document): Boolean = {
        val buttons: Elements = doc.select(cssSelector)
        buttons.size() > 0 && buttons.first().text() == textComparator
    }
}

class NeweggProduct(
  val name: String,
  val url: String
) extends Product {
    override def isInStock(doc: Document): Boolean = ???
}

class EvgaProduct(
  val name: String,
  val url: String
) extends Product {
    override def isInStock(doc: Document): Boolean = ???
}
