package http4slearning.models


case class UserForm(username: String, email: String, age: Int)
case class UserAgeForm(age: Int)
case class User(id: Long, username: String, email: String, age: Int)

