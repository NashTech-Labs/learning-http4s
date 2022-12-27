name := "Hands-on-with-http4s"

version := "0.1"

scalaVersion := "2.13.10"

val http4sVersion = "0.23.11"

lazy val `http4s-tutorial` = project
.in(file("."))
  .aggregate(
    `http4s-tutorial-server`,
    `http4s-tutorial-client`
  )
  .settings(
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      "org.http4s" %% "http4s-blaze-client" % http4sVersion,
      "com.typesafe" % "config" % "1.2.1",
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "io.circe" %% "circe-generic" % "0.14.3"
    )
  )


lazy val `http4s-tutorial-server` = project.in(file("modules/Http4sTutorialServer"))
  .settings(
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      "org.http4s" %% "http4s-blaze-client" % http4sVersion,
      "com.typesafe" % "config" % "1.2.1",
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "io.circe" %% "circe-generic" % "0.14.3"
    )
  )
lazy val `http4s-tutorial-client` = project.in(file("modules/Http4sTutorialClient"))
  .settings(
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      "org.http4s" %% "http4s-blaze-client" % http4sVersion,
      "com.typesafe" % "config" % "1.2.1",
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "io.circe" %% "circe-generic" % "0.14.3",
      "io.circe" %% "circe-literal" % "0.14.3"
    )
  )

scalacOptions += "-Ypartial-unification"

