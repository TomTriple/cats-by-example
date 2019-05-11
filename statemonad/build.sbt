import Dependencies._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings( 
    name := "statemonad",
    resolvers += Resolver.sonatypeRepo("releases"), 
    libraryDependencies += scalaTest % Test, 
    scalacOptions += "-Ypartial-unification",
    addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.0"),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.0-M4"), 
    libraryDependencies += "org.typelevel" %% "cats-core" % "1.6.0", 
    libraryDependencies += "org.typelevel" %% "cats-effect" % "1.3.0"
  )


