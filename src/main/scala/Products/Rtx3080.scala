package Products

import domain.{Product, ProductLocation}

object Rtx3080 {
  val bestBuyRtx3080: ProductLocation = ProductLocation(
    "bestBuy",
    "https://www.bestbuy.com/site/nvidia-geforce-rtx-3080-10gb-gddr6x-pci-express-4-0-graphics-card-titanium-and-black/6429440.p?skuId=6429440",
    "button.add-to-cart-button",
    "Add to Cart"
  )

  val newEggRtx3080: ProductLocation = ProductLocation(
    "newEgg",
    "https://www.newegg.com/msi-geforce-rtx-3080-rtx-3080-ventus-3x-10g/p/N82E16814137600?Item=N82E16814137600&quicklink=true",
    "#ProductBuy button.btn-wide",
    "Add to cart"
  )

  val rtx3080: Product = Product(
    "rtx3080",
    Seq(bestBuyRtx3080, newEggRtx3080)
  )
}
