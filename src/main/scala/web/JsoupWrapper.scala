package web

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

trait JsoupWrapper {
    def connectAndGet(url: String): Document
}

class JsoupWrapperImpl() extends JsoupWrapper {
    override def connectAndGet(url: String): Document = {
        Jsoup
            .connect(url)
            .get()
    }
}
