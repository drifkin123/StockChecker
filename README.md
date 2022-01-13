# Stock Checker
With the current graphics card shortage, one can only dream of getting their hands on a shiny RTX 3080... for msrp.
Until now! 

This program will check (currently only Best Buy's products) the stock of the given URLs every 10 seconds (approximately), and if the item is in stock then you will receive a text message with the url to buy the product.

This program does NOT checkout for you... it simply lets you know if an item is in stock

## Setup

In order to recieve texts, set up a [Twilio](https://www.twilio.com/) account.

Build a fat jar by running `sbt assmbly` in the root directory. This will create a jar in `target/scala-2.13/WebsiteStockNotifier.jar`

> ### OR

There is an already build fat jar [here](build/WebsiteStockNotifier.jar)

## Usage

Export env variables:
```bash
export ACCOUNT_SID=ACCOUNT_SID_FROM_TWILIO
export AUTH_TOKEN=AUTH_TOKEN_FROM_TWILIO
export TWILIO_PHONE_NUMBER=TWILIO_PHONE_NUMBER_FROM_TWILIO
export OUTBOUND_NUMBER=PHONE_NUMBER_TO_NOTIFY
java -jar WebsiteStockNotifier.jar
```

And let it do it's thing!


