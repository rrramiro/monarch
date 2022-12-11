package monarch.system.db

import zio._

import java.sql.Connection
import javax.sql.DataSource

object DatasourceUtils {

  def waitConnection: ZIO[Scope with DataSource, Throwable, Unit] =
    ZIO
      .service[DataSource]
      .flatMap(ds =>
        ZIO
          .acquireRelease(ZIO.attempt(ds.getConnection))(conn =>
            ZIO.attempt(conn.close()).ignoreLogged
          )
          .tapError(t =>
            ZIO.logWarning(
              s"Failed to establish a connection to Postgres. Cause: ${t.getMessage})"
            )
          )
          .flatMap(checkConnection)
          .retry(Schedule.recurs(10) andThen Schedule.spaced(1.second))
      ) *> ZIO.logInfo("Connection established to Postgres")

  private def checkConnection(conn: Connection): Task[Unit] =
    ZIO.attemptBlocking {
      val stmt = conn.createStatement()
      val rs = stmt.executeQuery("SELECT 1")
      rs.next()
      val i = rs.getInt(1)
      if (i != 1)
        throw new Exception(s"Postgres query check failed, expected 1, got $i")
    }

}
