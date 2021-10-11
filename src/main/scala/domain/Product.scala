package domain

case class ProductLocation(
  store: String,
  url: String,
  cssSelector: String,
  textComparator: String
)

case class Product(
  name: String,
  productLocations: Seq[ProductLocation]
)
