package monarch.system.db

import monarch.system.config.Configuration
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import zio._

import javax.sql.DataSource

object DatasourceLive {
  val layer: URLayer[Configuration with Scope, DataSource] = ZLayer.fromZIO {
    ZIO.service[Configuration].flatMap { config =>
      ZIO.acquireRelease(
        ZIO.succeed(
          new HikariDataSource(
            new HikariConfig {
              setJdbcUrl(config.dbConfig.url)
              setUsername(config.dbConfig.user)
              setPassword(config.dbConfig.password)
            }
          )
        )
      )(p => ZIO.attempt(p.close()).orDie)
    }
  }
}
