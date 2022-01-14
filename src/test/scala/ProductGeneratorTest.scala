import domain.{BestBuyProduct, NeweggProduct, EvgaProduct}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import domain.Product

class ProductGeneratorTest extends AnyFunSuite with Matchers {
    test("Should create products for urls") {
        // given
        val expectedProduct = new BestBuyProduct("The card", "https://www.bestbuy.com")
        val pathToProduct = getClass.getResource("/products").getPath

        // when
        val products = ProductGenerator.generateProducts(pathToProduct)

        // then
        products.head.name shouldBe expectedProduct.name
        products.head.url shouldBe expectedProduct.url
    }

    test("Should create urls for products from different websites") {
        // given
        val expectedNewegg = new NeweggProduct("RTX 3060 gv", "https://www.newegg.com/gigabyte-geforce-rtx-3060-gv-n3060eagle-12gd/p/N82E16814932464?Item=N82E16814932464&quicklink=true")
        val expectedEvga = new EvgaProduct("RTX 3060 XC", "https://www.evga.com/products/product.aspx?pn=12G-P5-3655-KR")

        val pathToProduct = getClass.getResource("/productsMultipleDomains.txt").getPath

        // when
        val products: Seq[Product] = ProductGenerator.generateProducts(pathToProduct)

        // then
        products.last.equals(expectedNewegg) shouldBe true
        products.head.equals(expectedEvga) shouldBe true
    }
}
