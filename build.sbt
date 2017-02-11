
name := "adverts"

version := "1.0"

scalaVersion := "2.11.8"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .disablePlugins(PlayLayoutPlugin)

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws % Test,
  specs2 % Test,
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.scalatestplus" %% "play" % "1.4.0" % "test",
  "com.typesafe.akka" %% "akka-testkit" % "2.4.1" % "test",
  "org.scalacheck" %% "scalacheck" % "1.12.1" % "test",
  "com.typesafe.play" %% "play" % "2.5.4",
  "com.typesafe.play" %% "play-json" % "2.5.4",
  "junit" % "junit" % "4.11" % "test",
  "com.typesafe.slick" %% "slick" % "3.0.0",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.1.1",
  "com.h2database" % "h2" % "1.3.193"
)

