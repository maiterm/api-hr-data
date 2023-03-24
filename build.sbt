name := """rrhh"""
organization := "com.mrm"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.10"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test

libraryDependencies ++= Seq(
  "com.h2database"  %  "h2"                           % "1.4.200", // your jdbc driver here
  "org.scalikejdbc" %% "scalikejdbc"                  % "4.0.0",
  "org.scalikejdbc" %% "scalikejdbc-config"           % "4.0.0",
  "org.scalikejdbc" %% "scalikejdbc-play-dbapi-adapter" % "2.8.0-scalikejdbc-3.5",
  "org.scalikejdbc" %% "scalikejdbc-play-initializer" % "2.8.0-scalikejdbc-3.5",
)
libraryDependencies += "org.scalikejdbc" %% "scalikejdbc-play-fixture" % "2.8.0-scalikejdbc-3.5"

libraryDependencies +="com.microsoft.sqlserver" % "mssql-jdbc" % "9.4.0.jre11"
//
//libraryDependencies += "com.typesafe.play" %% "play-jdbc" % "2.8.8"


enablePlugins(ScalikejdbcPlugin)

dependencyOverrides += "org.scala-lang.modules" %% "scala-parser-combinators" % "2.1.0"
// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.mrm.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.mrm.binders._"
