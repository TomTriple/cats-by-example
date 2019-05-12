import Dependencies._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "de.tomhoefer.fpinscala"
ThisBuild / organizationName := "example"

lazy val root = (project in file(".")) 
  .settings( 
    name := "either",
    resolvers += Resolver.sonatypeRepo("releases"), 
    libraryDependencies += "org.typelevel" %% "cats-core" % "1.6.0", 
    libraryDependencies += "org.typelevel" %% "cats-effect" % "1.3.0"
  )


