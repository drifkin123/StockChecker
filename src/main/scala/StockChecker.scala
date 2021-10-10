import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

import scala.util.Try

class StockChecker {
  def hasStock(product: Product): Try[Boolean] = Try {
    val doc: Document = Jsoup
      .connect(product.url)
      .get()

    val button: Elements = doc.select(product.cssSelector)

    button.first().text() == "Add to Cart"
  }
}
