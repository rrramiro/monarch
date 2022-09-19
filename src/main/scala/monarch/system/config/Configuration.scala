package monarch.system.config

import pureconfig._
import pureconfig.generic.derivation.default._
import zio._

case class Configuration(
    httpServer: HttpServerConfig,
    dbConfig: PostgresConfig
) derives ConfigReader

object ConfigurationLive {

  private val basePath = "monarch"
  private val source = ConfigSource.default.at(basePath)

  def layer: ULayer[Configuration] = ZLayer.fromZIO(
    ZIO
      .attempt(source.loadOrThrow[Configuration])
      .orDie
  )
}
