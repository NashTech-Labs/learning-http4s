name := "Hands-on-with-http4s"

version := "0.1"

ThisBuild / scalaVersion := "2.13.10"

val http4sVersion = "1.0.0-M37"
val circeVersion = "0.14.3"
resolvers += Resolver.sonatypeRepo("snapshots")

val commonLibDependencies = Seq (
  libraryDependencies ++= Seq(
    "org.http4s" %% "http4s-dsl" % http4sVersion,
    "org.http4s" %% "http4s-ember-server" % http4sVersion,
    "org.http4s" %% "http4s-ember-client" % http4sVersion,
    "com.typesafe" % "config" % "1.2.1",
    "org.http4s" %% "http4s-circe" % http4sVersion,
    "io.circe" %% "circe-generic" % circeVersion
  )
)

lazy val `http4s-tutorial` = project
.in(file("."))
  .aggregate(
    `http4s-tutorial-server`,
    `http4s-tutorial-client`
  )


lazy val `http4s-tutorial-server` = project.in(file("modules/Http4sTutorialServer"))
  .settings(
    commonLibDependencies
  )

lazy val `http4s-tutorial-client` = project.in(file("modules/Http4sTutorialClient"))
  .settings(
    commonLibDependencies,
    libraryDependencies += "io.circe" %% "circe-literal" % circeVersion
  )

//scalacOptions += "-Ypartial-unification"

