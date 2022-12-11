package monarch.system.config

import pureconfig._
import pureconfig.generic.auto._
import zio._

case class Configuration(
    httpServer: HttpServerConfig,
    dbConfig: DatabaseConfig
)

object ConfigurationLive {

  private val basePath = "monarch"
  private val source = ConfigSource.default.at(basePath)
  def layer: ULayer[HttpServerConfig with DatabaseConfig] =
    ZLayer.fromZIOEnvironment(
      ZIO
        .attempt(source.loadOrThrow[Configuration])
        .orDie
        .map(conf =>
          ZEnvironment[HttpServerConfig, DatabaseConfig](
            conf.httpServer,
            conf.dbConfig
          )
        )
    )
}
