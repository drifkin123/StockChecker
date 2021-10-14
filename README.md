# Stock Checker

This project (currently in its infant stage) will check the stock of the given products, and notify the user when the item(s) are in stock. 

## Installation

Set up a free account with [Twilio](https://www.twilio.com/)

Install [sbt](https://www.scala-sbt.org/) (Scala Build Tool)

## Usage

Export env variables:
```bash
export ACCOUNT_SID=ACCOUNT_SID_FROM_TWILIO
export AUTH_TOKEN=AUTH_TOKEN_FROM_TWILIO
export TWILIO_PHONE_NUMBER=TWILIO_PHONE_NUMBER_FROM_TWILIO
export OUTBOUND_NUMBER=PHONE_NUMBER_TO_NOTIFY
```

In terminal, go to `Main.scala` location
```
sbt run
```