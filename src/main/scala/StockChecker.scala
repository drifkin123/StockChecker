import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class StockChecker {

  def hasStock(url: String, cssSelector: String): Boolean = {
    val doc: Document = Jsoup.connect(url).get()
    //
    val button: Elements = doc.select(cssSelector)

    button.first().text() == "Add to Cart"
  }
}
