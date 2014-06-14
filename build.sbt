name := "Hue Flicker"

version := "1.0"

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.11" % "2.2.0" % "test",
  "org.scalaj" %% "scalaj-http" % "0.3.15",
  "org.json4s" %% "json4s-native" % "3.2.10"
)