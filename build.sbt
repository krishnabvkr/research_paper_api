name := """research-paper-api"""
version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(Common, PlayService, PlayLayoutPlugin)

scalaVersion in ThisBuild := "2.12.8"

libraryDependencies += guice
libraryDependencies += "net.codingwell" %% "scala-guice" % "4.2.1"

