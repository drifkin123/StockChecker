import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class StockChecker {
  def hasStock(product: Product): Boolean = {
    val doc: Document = Jsoup.connect(product.url).get()

    val button: Elements = doc.select(product.cssSelector)

    button.first().text() == "Add to Cart"
  }
}
