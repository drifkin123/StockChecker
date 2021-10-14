import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import org.jsoup.select.Elements
import domain.Product

import scala.util.Try

class StockChecker(product: Product) {
  def hasStock(): Try[Seq[String]] = Try {

    product.productLocations.map(productLocation => {
      val doc: Document = Jsoup
        .connect(productLocation.url)
        .get()

      val buttons: Elements = doc.select(productLocation.cssSelector)

      if (buttons.size() > 0) {
        (buttons.first().text() == productLocation.textComparator, productLocation.url)
      } else {
        (false, productLocation.url)
      }
    })
      .filter {
        case (inStock, _) => inStock
      }
      .map {
        case (_, url) => url
      }
  }
}
