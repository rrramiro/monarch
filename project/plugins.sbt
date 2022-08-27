addDependencyTreePlugin

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.6")

addSbtPlugin("ch.epfl.scala" % "sbt-missinglink" % "0.3.3")

addSbtPlugin("net.vonbuchholtz" % "sbt-dependency-check" % "4.1.0")

addSbtPlugin("com.github.cb372" % "sbt-explicit-dependencies" % "0.2.16")

addSbtPlugin("org.jmotor.sbt" % "sbt-dependency-updates" % "1.2.6")

lazy val jacksonVersion = "2.9.9"
dependencyOverrides ++= Seq(
  "org.scala-lang.modules" % "scala-xml_2.12" % "2.1.0",
  "com.fasterxml.jackson.module" % "jackson-module-scala_2.12" % jacksonVersion,
  "com.fasterxml.jackson.core" % "jackson-annotations" % jacksonVersion,
  "com.fasterxml.jackson.core" % "jackson-core" % jacksonVersion,
  "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion,
  "com.fasterxml.jackson.dataformat" % "jackson-dataformat-cbor" % jacksonVersion,
  "com.fasterxml.jackson.datatype" % "jackson-datatype-jdk8" % jacksonVersion,
  "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % jacksonVersion,
  "com.fasterxml.jackson.module" % "jackson-module-parameter-names" % jacksonVersion
)
