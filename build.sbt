val zioVersion    = "2.0.2"
val ziologgingVersion = "2.0.1"
val izumiReflectVersion = "2.1.3"
val catsVersion = "2.8.0"
val catsEffectVersion = "3.3.14"
val fs2Version = "3.2.12"
val shapelessVersion = "2.3.9"
val quillVersion  = "4.4.1"
val tapirVersion  = "1.1.0"
val pureconfigVersion = "0.17.1"
val kindProjectorVersion   = "0.13.2"
val bmfVersion             = "0.3.1"
val zioInteropCatsVersion = "3.3.0"
val zioMockVersion = "1.0.0-RC8"
val logbackVersion = "1.2.11"
val http4sVersion = "0.23.15"
val http4sServerVersion = "0.23.12"
val refinedVersion = "0.10.1"
val circeVersion = "0.14.3"
val flywayVersion = "8.5.1"
val slf4jVersion = "1.7.36"
val postgresqlVersion = "42.2.25"

lazy val root = project
  .in(file("."))
  .settings(
    name := "monarch",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := "2.13.8",
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
      "dev.zio" %% "zio-stacktracer" % zioVersion,
      "dev.zio" %% "izumi-reflect" % izumiReflectVersion,
      //"dev.zio" %% "zio-managed" % zioVersion,
      "dev.zio" %% "zio-logging-slf4j" % ziologgingVersion,
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
      "com.softwaremill.sttp.apispec" %% "openapi-model" % "0.2.1",
      "com.softwaremill.sttp.model" %% "core" % "1.5.0",
      "com.softwaremill.sttp.shared" %% "zio" % "1.3.7",
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

      "dev.zio" %% "zio-test" % zioVersion % "test",
      "dev.zio" %% "zio-test-sbt" % zioVersion % "test",
      "dev.zio" %% "zio-mock" % "1.0.0-RC8" % "test",
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

Global / onChangedBuildSource := ReloadOnSourceChanges
