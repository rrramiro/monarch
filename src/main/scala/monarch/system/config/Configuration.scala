package monarch.system.config

import monarch.http.HttpServerConfig
import monarch.system.db.HikariConfig
import pureconfig._
import pureconfig.generic.auto._
import zio._

final case class Configuration(
    httpServer: HttpServerConfig,
    dbConfig: com.typesafe.config.Config
)

object ConfigurationLive {

  private val basePath = "monarch"
  private val source = ConfigSource.default.at(basePath)
  def layer: ULayer[HttpServerConfig with HikariConfig] =
    ZLayer.fromZIOEnvironment(
      ZIO
        .attempt(source.loadOrThrow[Configuration])
        .orDie
        .map(conf =>
          ZEnvironment[HttpServerConfig, HikariConfig](
            conf.httpServer,
            HikariConfig(conf.dbConfig)
          )
        )
    )
}
