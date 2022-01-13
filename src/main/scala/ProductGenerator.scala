import domain.{BestBuyProduct, Product}

import scala.io.Source

object ProductGenerator {
    def generateProducts(pathToProducts: String): Seq[Product] = {
        val source = Source.fromFile(pathToProducts)

        var products: Seq[Product] = Seq()

        for (line <- source.getLines) {
            val lineItem = line.split("!")
            val (productName, productUrl) = (lineItem(0), lineItem(1))

            val domainRegex = raw"https:\/\/www.(\D+).com.*".r
            val domainName = productUrl match {
                case domainRegex(domain) => domain
            }

            domainName match {
                case "bestbuy" => products = products.+:(new BestBuyProduct(productName, productUrl))
            }
        }

        source.close()
        products
    }
}
