package http4slearning

import cats.effect.kernel.Ref
import cats.effect.{ExitCode, IO, IOApp}
import com.typesafe.config.{Config, ConfigFactory}
import fs2.concurrent.SignallingRef

object Runner extends IOApp {

  implicit private val conf: Config = ConfigFactory.load()

  private val signallingRef: IO[SignallingRef[IO, Boolean]] = SignallingRef[IO, Boolean](false)

  override def run(args: List[String]): IO[ExitCode] =
    for {
      signal <- signallingRef
      exitCode     <- Ref[IO].of(cats.effect.ExitCode.Error)
      _ <- new HttpServer(conf).server(signal, exitCode)
    } yield (ExitCode.Success)
}
