import sbt._

object Dependencies {

  object Versions {
    val cats       = "2.6.1"
    val catsEffect = "2.5.1"
    val fs2        = "2.5.4"
    val http4s     = "0.22.15"
    val circe      = "0.14.2"
    val pureConfig = "0.17.4"

    val kindProjector  = "0.13.2"
    val logback        = "1.2.3"
    val scalaCheck     = "1.15.3"
    val scalaTest      = "3.2.7"
    val catsScalaCheck = "0.3.2"

    val sttp           = "4.0.0-M6"
    val scaffeine      = "5.3.0"
  }

  object Libraries {
    def circe(artifact: String): ModuleID  = "io.circe"   %% artifact % Versions.circe
    def http4s(artifact: String): ModuleID = "org.http4s" %% artifact % Versions.http4s

    lazy val cats       = "org.typelevel" %% "cats-core"   % Versions.cats // Functional programming library for Scala
    lazy val catsEffect = "org.typelevel" %% "cats-effect" % Versions.catsEffect // The pure asynchronous runtime for Scala
    lazy val fs2        = "co.fs2"        %% "fs2-core"    % Versions.fs2 // Compositional, streaming I/O library for Scala

    lazy val http4sDsl       = http4s("http4s-dsl") // HTTP DSL
    lazy val http4sServer    = http4s("http4s-blaze-server")  // Blaze server
    lazy val http4sCirce     = http4s("http4s-circe") // Circe support for http4s
    lazy val circeCore       = circe("circe-core") // JSON library for Scala
    lazy val circeGeneric    = circe("circe-generic") // Generic support for Circe
    lazy val circeGenericExt = circe("circe-generic-extras") // Additional generic support for Circe
    lazy val circeParser     = circe("circe-parser") // JSON parser for Circe
    lazy val pureConfig      = "com.github.pureconfig" %% "pureconfig" % Versions.pureConfig // A boilerplate-free Scala library for loading configuration files

    // Compiler plugins
    lazy val kindProjector = "org.typelevel" %% "kind-projector" % Versions.kindProjector cross CrossVersion.full

    // Runtime
    lazy val logback = "ch.qos.logback" % "logback-classic" % Versions.logback // Logging framework

    // HTTP client
    lazy val sttp = "com.softwaremill.sttp.client4" %% "core" % Versions.sttp // The Scala HTTP client 

    // scaffeine cache
    lazy val scaffeine = "com.github.blemale" %% "scaffeine" % Versions.scaffeine

    // Test
    lazy val scalaTest      = "org.scalatest"     %% "scalatest"       % Versions.scalaTest
    lazy val scalaCheck     = "org.scalacheck"    %% "scalacheck"      % Versions.scalaCheck
    lazy val catsScalaCheck = "io.chrisdavenport" %% "cats-scalacheck" % Versions.catsScalaCheck
  }

}
