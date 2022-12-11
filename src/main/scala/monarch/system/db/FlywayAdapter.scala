package monarch.system.db

import org.flywaydb.core.Flyway
import zio._

import javax.sql.DataSource

object FlywayAdapter {
  def migrate(): ZIO[DataSource, Throwable, Unit] =
    ZIO
      .service[DataSource]
      .flatMap(ds =>
        ZIO
          .attempt(Flyway.configure().dataSource(ds).load().migrate())
          .as(())
          .orDie
      )

}
