package monarch.system.db

import com.zaxxer.hikari.HikariDataSource
import zio._

import javax.sql.DataSource

object DatasourceLive {
  val layer: URLayer[HikariConfig with Scope, DataSource] = ZLayer.fromZIO {
    ZIO.service[HikariConfig].flatMap ( config =>
      ZIO.acquireRelease(ZIO.succeed(new HikariDataSource(config)))(p => ZIO.attempt(p.close()).orDie)
    )
  }
}
