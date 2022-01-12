name := "WebsiteStockNotifier"

version := "0.1"

scalaVersion := "2.13.6"

libraryDependencies ++= Seq(
  "com.typesafe"   % "config" % "1.3.2",
  "com.twilio.sdk" % "twilio" % "7.15.5",
  "org.jsoup" % "jsoup" % "1.14.1"
)

// https://mvnrepository.com/artifact/org.scalatest/scalatest
libraryDependencies += "org.scalatest" % "scalatest_2.13" % "3.2.9"
