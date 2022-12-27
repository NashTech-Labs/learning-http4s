package http4slearning

import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.client.Client
import org.http4s.implicits.http4sLiteralsSyntax
import cats.effect.unsafe.implicits.global
import io.circe.literal._
import org.http4s.{Method, Request, Uri}
import org.http4s.circe._
import org.http4s.ember.client.EmberClientBuilder

object Runner extends IOApp {

  def healthCheck(client: Client[IO]): IO[String] =
    client.expect[String](uri"http://localhost:8086/healthcheck")

  def getAllUsers(client: Client[IO], sortOrder: Option[String] = None) =
    sortOrder match {
      case Some(sorting) => client.expect[String](s"http://localhost:8086/users?sort=$sorting")
      case None => client.expect[String](uri"http://localhost:8086/users")
    }

  def getUser(client: Client[IO], userId: String) =
    client.expect[String](uri"http://localhost:8086/users/" /userId)

  val body = json"""{"username":"John Doe", "email":"john.doe@gmail.com", "age":29}"""

  def postRequest(client: Client[IO]) = Request[IO](method = Method.POST, uri = Uri.unsafeFromString("http://localhost:8086/users"))
    .withEntity(body)

  val bodyAge = json"""{"age":65}"""

  def putRequest(client: Client[IO], userId: String) =
    Request[IO](method = Method.PUT, uri = Uri.unsafeFromString(s"http://localhost:8086/users/$userId"))
    .withEntity(bodyAge)

  def deleteRequest(client: Client[IO], userId: String) = Request[IO](method = Method.DELETE,
    uri = Uri.unsafeFromString(s"http://localhost:8086/users/$userId"))


  override def run(args: List[String]): IO[ExitCode] =
    EmberClientBuilder.default[IO].build
    .use { client =>

      // health-check
//      println(healthCheck(client).unsafeRunSync())

      // Get UserID Specific information
//      println(getUser(client, "1").unsafeRunSync())

      // Get All Users
      println(getAllUsers(client, Some("desc")).unsafeRunSync())

      // POST a new user
      /*val req = postRequest(client)
      val t = client.expect[String](req).unsafeRunSync()
      println(t)*/

      // Update an existing users age
      /*val req = putRequest(client, "975")
      val t = client.expect[String](req).unsafeRunSync()
      println(t)*/

      // Deleting an existing user
      /*val req = deleteRequest(client, "923")
      val t = client.expect[String](req).unsafeRunSync()
      println(t)*/
      IO.unit
    }
    .as(ExitCode.Success)
}
