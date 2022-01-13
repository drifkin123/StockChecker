import domain.BestBuyProduct
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

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
}
