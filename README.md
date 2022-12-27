# learning-http4s


Using Http4s support for ember server and client builder, we have created an http server running by default on localhost:8086.

In this repo, we have created routes to read information for all users from a list, add a new user entry to the list, update an existing user, and delete one if required.

#### Routes Defined
##### HealthCheck
Route: `http://host:port/healthcheck` <br>
Response: `{"status":true,"uptime":47}` <br>
Route Type: Get

##### Get All Users
Route: `http://host:port/users?sort=desc` <br>
Default sorting: Asc on username, if no sort parameter is provided <br>
Response: `[{"id":1,"username":"John","email":"john@gmail.com","age":29},{"id":2,"username":"Anita","email":"anita.g@gmail.com","age":58}]`<br>
Route Type: Get

##### Get info for a specific user id
Route: `http://host:port/users/1` <br>
Response: `{"id":1,"username":"John","email":"john@gmail.com","age":29}` <br>
Route Type: Get

##### Post a new User Request
Request Body: `{"username":"John Doe", "email":"john.doe@gmail.com", "age":29}` <br>
Route: `http://host:port/users` <br>
Response: `User with id: <id> created` <br>
Route Type: Post

##### Update existing User
Request Body: `{"age":65}` <br>
Route: `http://host:port/users/<userId>` <br>
Route Type: PUT

##### Delete an existing User
Route: `http://host:port/users/<userid>` <br>
Route Type: DELETE

Note: All the configurable values in the routes (userId, requestbody) can be updated in Runner.scala in modules/Http4sTutorialClient/

#### Running Server and Client
1. clone the repo
2. Go to the root of the repo
3. To run server:
`sbt "project http4s-tutorial-server" run` <br>
4. To run client:
`sbt "project http4s-tutorial-client" run`