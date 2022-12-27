package http4slearning.services

import java.lang.management.ManagementFactory

import http4slearning.HealthCheckStatus

import scala.concurrent.duration.{Duration, MILLISECONDS}

object HealthCheckService {
  val getHealthCheckInfo: HealthCheckStatus =
    HealthCheckStatus(
      status = true,
      Duration(ManagementFactory.getRuntimeMXBean.getUptime, MILLISECONDS).toSeconds,
    )
}
