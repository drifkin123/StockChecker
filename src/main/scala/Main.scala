import com.twilio.Twilio
import com.twilio.`type`.PhoneNumber
import com.twilio.rest.api.v2010.account.Message
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

object Main extends App {

  val ACCOUNT_SID = "ACb7471bd44057d5a044a05ae943ebc7a5"
  val AUTH_TOKEN = "7b49604d801c8ee1e38505c33745a2b2"
  val TWILIO_PHONE_NUMBER = "+12704798203"
  val OUTBOUND_NUMBER = "+18186655087"
  val URL_TO_CHECK = "https://www.bestbuy.com/site/nvidia-geforce-rtx-3080-10gb-gddr6x-pci-express-4-0-graphics-card-titanium-and-black/6429440.p?skuId=6429440"
  Twilio.init(ACCOUNT_SID, AUTH_TOKEN)

  while (true) {
    val isInStock = hasStock

    if (isInStock) {
      nofity()
    } else {
      println("Unfortunately not :(")
    }

    Thread.sleep(5 * 1000)
  }

  def hasStock: Boolean = {
    val doc: Document = Jsoup.connect(URL_TO_CHECK).get()

    val button: Elements = doc.select("button.add-to-cart-button")

    button.first().text() == "Add to Cart"
  }

  def nofity(): Unit = {
    println("SHOULD BE NOTIFYING...")
    val from = new PhoneNumber(TWILIO_PHONE_NUMBER)
    val to = new PhoneNumber(OUTBOUND_NUMBER)
    val body = "BUY THE GRAPHICS CARD RIGHT NOW"

    Message.creator(to, from, body).create()
  }
}
