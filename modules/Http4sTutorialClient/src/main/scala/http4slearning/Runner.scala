package http4slearning

import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.client.Client
import cats.effect.unsafe.implicits.global
import com.typesafe.config.{Config, ConfigFactory}
import http4slearning.util.CONSTANT
import io.circe.literal._
import org.http4s.{Method, Request, Uri}
import org.http4s.circe._
import org.http4s.ember.client.EmberClientBuilder

object Runner extends IOApp {

  val config: Config = ConfigFactory.load()

  private val port   = config.getString(CONSTANT.SERVER_PORT).toInt

  private val host   = config.getString(CONSTANT.SERVER_HOST)

  val baseUri: Uri = Uri.unsafeFromString(s"http://$host:$port")

  def healthCheck(client: Client[IO]): IO[String] =
    client.expect[String](s"$baseUri/healthcheck")

  def getAllUsers(client: Client[IO], sortOrder: Option[String] = None): IO[String] =
    sortOrder match {
      case Some(sorting) => client.expect[String](s"$baseUri/users?sort=$sorting")
      case None => client.expect[String](s"$baseUri/users")
    }

  def getUser(client: Client[IO], userId: String): IO[String] =
    client.expect[String](s"$baseUri/users/$userId")

  val newUser = json"""{"username":"John Doe", "email":"john.doe@gmail.com", "age":29}"""

  def postRequest(client: Client[IO]): Request[IO] = Request[IO](
    method = Method.POST,
    uri = Uri.unsafeFromString(s"http://$host:$port/users")
  ).withEntity(newUser)

  val updatedAgeForExisting = json"""{"age":65}"""

  def putRequest(client: Client[IO], userId: String): Request[IO] =
    Request[IO](method = Method.PUT, uri = Uri.unsafeFromString(s"http://$host:$port/users/$userId"))
    .withEntity(updatedAgeForExisting)

  def deleteRequest(client: Client[IO], userId: String): Request[IO] = Request[IO](method = Method.DELETE,
    uri = Uri.unsafeFromString(s"http://$host:$port/users/$userId"))


  override def run(args: List[String]): IO[ExitCode] =
    EmberClientBuilder.default[IO].build
    .use { client =>

//      health-check
      println("Health Check for the server")
      println(healthCheck(client).unsafeRunSync())

      println("************************************************")
//      Get UserID Specific information
      val userIdToFetch = "1"
      println(s"Getting User Info for userID $userIdToFetch")
      println(getUser(client, userIdToFetch).unsafeRunSync())

      println("************************************************")
      //      Get All Users
      println("Initial State of User List")
      println(getAllUsers(client, Some("desc")).unsafeRunSync())

      println("************************************************")
      //      POST a new user
      println("Adding a new user to User List")
      val newUserReq = postRequest(client)
      val newUserResponse = client.expect[String](newUserReq).unsafeRunSync()
      println(newUserResponse)
      println("User List After Adding a New User")
      println(getAllUsers(client, Some("desc")).unsafeRunSync())

      println("************************************************")
//      Update an existing users age
      println("Updating existing user")
      val userIdToUpdate = "1"
      val updateUserReq = putRequest(client, userIdToUpdate)
      client.expect[String](updateUserReq).unsafeRunSync()
      println(s"User Info for userID $userIdToUpdate After updating age")
      println(getUser(client, userIdToUpdate).unsafeRunSync())

      println("************************************************")
      //      Deleting an existing user
      println("Deleting a user")
      val userIdToDelete = "2"
      val deleteUserReq = deleteRequest(client, userIdToDelete)
      client.expect[String](deleteUserReq).unsafeRunSync()
      println(s"User List after deleting userID $userIdToDelete")
      println(getAllUsers(client).unsafeRunSync())
      IO.unit
    }
    .as(ExitCode.Success)
}
