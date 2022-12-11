package monarch.system.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import monarch.system.config.DatabaseConfig
import zio._

import javax.sql.DataSource

object DatasourceLive {
  val layer: URLayer[DatabaseConfig with Scope, DataSource] = ZLayer.fromZIO {
    ZIO.service[DatabaseConfig].flatMap { config =>
      ZIO.acquireRelease(
        ZIO.succeed(
          new HikariDataSource(
            new HikariConfig {
              setDriverClassName(config.className)
              setJdbcUrl(config.url)
              setUsername(config.user)
              setPassword(config.password)
            }
          )
        )
      )(p => ZIO.attempt(p.close()).orDie)
    }
  }
}
