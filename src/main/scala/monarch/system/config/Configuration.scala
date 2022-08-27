package monarch.system.config

import pureconfig._
import pureconfig.generic.auto._
import zio._

case class Configuration(
    httpServer: HttpServerConfig,
    dbConfig: PostgresConfig
)

object ConfigurationLive {

  private val basePath = "monarch"
  private val source = ConfigSource.default.at(basePath)

  def layer: ULayer[Configuration] = ZLayer.fromZIO(
    ZIO
      .attempt(source.loadOrThrow[Configuration])
      .orDie
  )
}
