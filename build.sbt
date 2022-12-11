lazy val zioVersion    = "2.0.5"
lazy val zioLoggingVersion = "2.1.5"
lazy val izumiReflectVersion = "2.2.2"
lazy val catsVersion = "2.9.0"
lazy val catsEffectVersion = "3.4.1"
lazy val fs2Version = "3.3.0"
lazy val shapelessVersion = "2.3.10"
lazy val quillVersion  = "4.6.0"
lazy val tapirVersion  = "1.2.3"
lazy val pureconfigVersion = "0.17.2"
lazy val kindProjectorVersion   = "0.13.2"
lazy val bmfVersion             = "0.3.1"
lazy val zioInteropCatsVersion = "3.3.0"
lazy val zioMockVersion = "1.0.0-RC8"
lazy val logbackVersion = "1.2.11"
lazy val http4sVersion = "0.23.16"
lazy val http4sServerVersion = "0.23.12"
lazy val refinedVersion = "0.10.1"
lazy val circeVersion = "0.14.3"
lazy val flywayVersion = "8.5.1"
lazy val slf4jVersion = "1.7.36"
lazy val postgresqlVersion = "42.5.1"
lazy val testContainersVersion = "1.17.6"
lazy val testContainersScalaVersion = "0.40.12"

lazy val root = project
  .in(file("."))
  .configs(IntegrationTest)
  .settings(
    Defaults.itSettings,
    inConfig(IntegrationTest)(ScalafmtPlugin.scalafmtConfigSettings),
    scalafixConfigSettings(IntegrationTest)
  )
  .settings(
    name := "monarch",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := "2.13.10",
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    Compile / run / fork := true,
    scalacOptions ++= {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((3, _)) => Seq(
        "-Ykind-projector:underscores",
        "-explain",
        "-deprecation"
        )
        case Some((2, 13)) =>
          Seq(
            "-feature",
            "-deprecation",
            "-explaintypes",
            "-unchecked",
            "-encoding",
            "UTF-8",
            "-language:higherKinds",
            "-language:existentials",
            //"-Xfatal-warnings",
            "-Xlint:-infer-any,-byname-implicit,_",
            "-Xlog-implicits",
            "-Ywarn-value-discard",
            "-Ywarn-numeric-widen",
            "-Ywarn-extra-implicit",
            "-Ywarn-unused:_",
            "-Ymacro-annotations"
          )
        case _ => Seq.empty[String]
      }
    },
    //scalaModuleInfo ~= (_.map(_.withOverrideScalaVersion(true))),
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % zioVersion,
      "dev.zio" %% "zio-managed" % zioVersion,
      "dev.zio" %% "zio-stacktracer" % zioVersion,
      "dev.zio" %% "izumi-reflect" % izumiReflectVersion,
      //"dev.zio" %% "zio-managed" % zioVersion,
      "dev.zio" %% "zio-logging-slf4j" % zioLoggingVersion,
      "dev.zio" %% "zio-interop-cats" % zioInteropCatsVersion,
      "io.getquill" %% "quill-jdbc-zio" % quillVersion,
      "io.getquill" %% "quill-jdbc" % quillVersion,
      "io.getquill" %% "quill-sql" % quillVersion,
      "io.getquill" %% "quill-engine" % quillVersion,
      "org.http4s" %% "http4s-blaze-server" % http4sServerVersion,
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-core" % http4sVersion,
      "org.http4s" %% "http4s-server" % http4sVersion,
      "eu.timepit" %% "refined" % refinedVersion,
      "io.circe" %% "circe-refined" % circeVersion,
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server-zio" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-core" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-zio" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-refined" % tapirVersion,
      //"com.softwaremill.sttp.tapir" %% "tapir-sttp-client" % tapirVersion % "test,it",
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-client" % tapirVersion % "test,it",
      "com.softwaremill.sttp.apispec" %% "openapi-model" % "0.3.1",
      "com.softwaremill.sttp.model" %% "core" % "1.5.3",
      "com.softwaremill.sttp.shared" %% "zio" % "1.3.12",
      "com.github.pureconfig" %% "pureconfig-core" % pureconfigVersion,
      "co.fs2" %% "fs2-core" % fs2Version,
      "co.fs2" %% "fs2-io" % fs2Version,
      "org.typelevel" %% "cats-core" % catsVersion,
      "org.typelevel" %% "cats-effect" % catsEffectVersion,
      "org.typelevel" %% "cats-effect-kernel" % catsEffectVersion,

      "com.zaxxer" % "HikariCP" % "3.4.5",
      "org.flywaydb" % "flyway-core" % flywayVersion,
      "ch.qos.logback" % "logback-classic" % logbackVersion,
      "org.slf4j" % "log4j-over-slf4j" % slf4jVersion,
      "org.postgresql" % "postgresql" % postgresqlVersion,

      "dev.zio" %% "zio-test" % zioVersion % "test,it",
      "dev.zio" %% "zio-test-sbt" % zioVersion % "test,it",
      "dev.zio" %% "zio-test-magnolia" % zioVersion % "test,it",
      //"dev.zio" %% "zio-mock" % "1.0.0-RC9" % "test,it",
      "org.testcontainers" % "testcontainers" % testContainersVersion % "test,it",
      "org.testcontainers" % "database-commons" % testContainersVersion % "test,it",
      "org.testcontainers" % "postgresql" % testContainersVersion % "test,it",
      "org.testcontainers" % "jdbc" % testContainersVersion % "test,it",
      //"io.github.scottweaver" %% "zio-2-0-db-migration-aspect" % "0.9.0" % "test,it",
      //"io.github.scottweaver" %% "zio-2-0-testcontainers-postgresql" % "0.9.0" % "test,it",
      "com.dimafeng" %% "testcontainers-scala-postgresql" % testContainersScalaVersion % "test,it"
    ) ++ {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, 13)) =>
          Seq(
            "com.chuusai" %% "shapeless" % shapelessVersion,
            "com.softwaremill.magnolia1_2" %% "magnolia" % "1.1.2",
            "io.getquill" %% "quill-core" % quillVersion,
            "com.github.pureconfig" %% "pureconfig-generic-base" % pureconfigVersion,
            "com.github.pureconfig" %% "pureconfig-generic" % pureconfigVersion,
            compilerPlugin("com.olegpy" %% "better-monadic-for" % bmfVersion),
            compilerPlugin(("org.typelevel" % "kind-projector" % kindProjectorVersion).cross(CrossVersion.full))
          )
          case _ =>  Seq.empty
      }
    },
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
    missinglinkIgnoreDestinationPackages ++= Seq(
      IgnoredPackage("sun.reflect")
    ),
    missinglinkIgnoreSourcePackages ++= Seq(
      IgnoredPackage("org.flywaydb.core"),
      IgnoredPackage("ch.qos.logback.core"),
      IgnoredPackage("com.zaxxer.hikari.metrics")
    )
  )

ThisBuild / scalafixDependencies ++= Seq("com.github.liancheng" %% "organize-imports" % "0.6.0")

Global / onChangedBuildSource := ReloadOnSourceChanges
