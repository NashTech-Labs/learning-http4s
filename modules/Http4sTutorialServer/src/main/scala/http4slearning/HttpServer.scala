package http4slearning

import java.util.concurrent.Executors

import cats.effect.{ExitCode, IO, Ref}
import com.comcast.ip4s._
import com.typesafe.config.Config
import fs2.concurrent.SignallingRef
import http4slearning.services.HttpService
import http4slearning.util.CONSTANT
import org.http4s.ember.server.EmberServerBuilder

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}
import cats.effect.unsafe.IORuntime

class HttpServer(conf: Config) {

  implicit val runtime: IORuntime = cats.effect.unsafe.IORuntime.global


  private val port   = conf.getString(CONSTANT.SERVER_PORT).toInt
  private val thread = conf.getString(CONSTANT.NUMBER_OF_THREAD).toInt
  private val host   = conf.getString(CONSTANT.SERVER_HOST)
  implicit val ec: ExecutionContextExecutor =
    ExecutionContext.fromExecutor(Executors.newFixedThreadPool(thread))

  /*def server(
    terminateWhenTrue:    SignallingRef[IO, Boolean],
    exitWith:             Ref[IO, ExitCode],
  ): IO[Unit] =
      BlazeServerBuilder[IO](executionContext = ec)
        .bindHttp(port, host)
        .withHttpApp(new HttpService().combinedRoutes)
        .serveWhile(terminateWhenTrue, exitWith)
        .compile
        .drain*/

  def server(
              terminateWhenTrue:    SignallingRef[IO, Boolean],
              exitWith:             Ref[IO, ExitCode],
            ): IO[Unit] =
    EmberServerBuilder
      .default[IO]
      .withHost(Host.fromString(host).get)
      .withPort(Port.fromInt(port).get)
      .withHttpApp(new HttpService().combinedRoutes)
      .build
    .use(_ => IO.never)
    .as(ExitCode.Success)

}
