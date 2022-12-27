package http4slearning.services

import cats.data.Kleisli
import cats.effect.IO
import http4slearning.models.{UserAgeForm, UserForm}
import http4slearning.services.UserService.{DuplicatedUsernameException, UserNotFoundException, UserSorting}
import io.circe._
import org.http4s.{HttpRoutes, Request, Response}
import org.http4s.dsl.io._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.implicits._

import scala.util.Random
//import org.http4s.circe._
import cats.syntax.semigroupk._
import http4slearning.models.User
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.Http4sDsl

class HttpService() {

  private val errorHandler: PartialFunction[Throwable, IO[Response[IO]]] = {
    case UserNotFoundException(id)              => BadRequest(s"User with id: $id not found!")
    case DuplicatedUsernameException(username)  => Conflict(s"Username $username already in use!")
  }

  private def getHealthCheckResponse[T](response: T)
                                       (implicit T: Encoder[T]): IO[Response[IO]] = {
    Ok(response.asJson)
  }


  object SortQueryParamMatcher extends OptionalQueryParamDecoderMatcher[String]("sort")

  val userServiceAPI = HttpRoutes
    .of[IO] {
      case GET -> Root /"users" :? SortQueryParamMatcher(sort) =>
        Ok(UserService.findAll(UserSorting.from(sort)))

      case GET -> Root / "users" / LongVar(id) =>
        Ok(UserService.find(id)).handleErrorWith(errorHandler)

      case req @ POST -> Root / "users" =>
        req.as[UserForm].flatMap { userForm =>
          val user = User(Random.nextInt(1000), userForm.username, userForm.email, userForm.age)
          UserService.save(user).flatMap(_ => Created(s"User with id: ${user.id} created")).handleErrorWith(errorHandler)
        }

      case req @ PUT -> Root / "users" / LongVar(id) =>
        req.as[UserAgeForm].flatMap { ageForm =>
          UserService.edit(id, ageForm.age).flatMap(_ => Accepted()).handleErrorWith(errorHandler)
        }

      case DELETE -> Root / "users" / LongVar(id) =>
        UserService.remove(id).flatMap(_ => NoContent()).handleErrorWith(errorHandler)
    }

  val healthCheckAPI = HttpRoutes
    .of[IO] {
      case GET -> Root / "healthcheck" => getHealthCheckResponse(HealthCheckService.getHealthCheckInfo)
    }

  val healthCheckRoutes = healthCheckAPI.orNotFound

  val combinedRoutes = (healthCheckAPI <+> userServiceAPI).orNotFound

}
