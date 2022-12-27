package http4slearning.services

import cats.effect.IO
import http4slearning.models.User
import http4slearning.services.UserService.UserSorting


object UserService extends UserService {

  private var usernames = scala.collection.mutable.Set[String]("John", "Anita")

  private var users = scala.collection.mutable.Map[Long, User](
    1L -> User(1L, "John", "john@gmail.com", 29),
    2L -> User(2L, "Anita", "anita.g@gmail.com", 58)
  )

  sealed trait UserSorting extends Product with Serializable
  case object Asc extends UserSorting
  case object Desc extends UserSorting

  object UserSorting {
    def from(sort: Option[String]): UserSorting = sort match {
      case Some(v) if v.equalsIgnoreCase("desc")  => Desc
      case _                                      => Asc
    }
  }

  case class UserNotFoundException(id: Long) extends Exception

  case class DuplicatedUsernameException(username: String) extends Exception

  private def add(user: User): IO[Unit] = IO.delay {
    usernames += user.username
    users += (user.id -> user)
  }

  override def save(user: User): IO[Unit] = {
    if (usernames.contains(user.username)) IO.raiseError(DuplicatedUsernameException(user.username))
    else add(user)
  }

  override def find(id: Long): IO[User] = users.get(id) match {
    case Some(user) => IO(user)
    case None => IO.raiseError(UserNotFoundException(id))
  }

  private def findAllUnsorted: IO[List[User]] = IO.delay { users.values.toList }


  override def findAll(sort: UserSorting): IO[List[User]] = sort match {
    case Asc  => findAllUnsorted.map(_.sortBy(_.username))
    case Desc => findAllUnsorted.map(_.sortBy(_.username).reverse)
  }

  override def remove(id: Long): IO[Unit] = users.get(id) match {
    case Some(user) => IO.delay {
      usernames = usernames -= user.username
      users = users -= id
    }
    case None       => IO.raiseError(UserNotFoundException(id))
  }

  override def edit(id: Long, age: Int): IO[Unit] =  users.get(id) match {
    case Some(user) => IO.delay { users.update(id, user.copy(age = age)) }
    case None       => IO.raiseError(UserNotFoundException(id))
  }
}


trait UserService {

  def save(user: User): IO[Unit]
  def findAll(sort: UserSorting): IO[List[User]]
  def find(id: Long): IO[User]
  def remove(id: Long): IO[Unit]
  def edit(id: Long, age: Int): IO[Unit]

}
