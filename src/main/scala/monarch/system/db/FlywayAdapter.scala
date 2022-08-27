package monarch.system.db

import zio._
import org.flywaydb.core.Flyway

import javax.sql.DataSource

trait FlywayAdapter {
  def migrate(): Task[Unit]
}

object FlywayAdapter {
  def migrate(): ZIO[FlywayAdapter, Throwable, Unit] =
    ZIO.serviceWithZIO[FlywayAdapter](_.migrate())
}

object FlywayAdapterLive {
  val layer: URLayer[DataSource, FlywayAdapter] = ZLayer.fromZIO {
    ZIO.service[DataSource].map { ds =>
      new FlywayAdapter {
        def migrate(): Task[Unit] = ZIO
          .attempt(
            new Flyway(
              Flyway.configure().dataSource(ds)
            ).migrate()
          )
          .as(())
      }
    }
  }
}
