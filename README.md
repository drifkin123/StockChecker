# Stock Checker
With the current graphics card shortage, one can only dream of getting their hands on a shiny RTX 3080... for msrp.
Until now! 

This program will check (currently only Best Buy's products) the stock of the given URLs every 10 seconds (approximately), and if the item is in stock then you will receive a text message with the url to buy the product.

This program does NOT checkout for you... it simply lets you know if an item is in stock.

## Setup

In order to recieve texts, set up a [Twilio](https://www.twilio.com/) account.

Build a fat jar by running `sbt assmbly` in the root directory. This will create a jar in `target/scala-2.13/WebsiteStockNotifier.jar`

> ### OR

There is an already built fat jar [here](build/WebsiteStockNotifier.jar)

#### Create text file with list of product names to urls
The text file that the program will read should have the format of the product name and url to the products location separated by !:
```
${product_name}!${url}
```
Example product file [here](src/main/resources/products)

To determine the url to enter (CURRENTLY ONLY SUPPORTS BEST BUY WEBSITE), go to the website page where the `Add to Cart` button is shown. But in our case, it will be `Sold Out` (In best buy's sake)

For example https://www.bestbuy.com/site/nvidia-geforce-rtx-3070-8gb-gddr6-pci-express-4-0-graphics-card-dark-platinum-and-black/6429442.p?skuId=6429442

## Usage

Export env variables:
```bash
export ACCOUNT_SID=...
export AUTH_TOKEN=...
export TWILIO_PHONE=...
export OUTBOUND_PHONE=...
export PATH_TO_PRODUCTS=...
java -jar WebsiteStockNotifier.jar
```

And let it do it's thing!


