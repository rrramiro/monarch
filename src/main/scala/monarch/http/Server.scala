package monarch.http

import cats.syntax.all._
import monarch.Environment.BootEnv
import monarch.Environment.CustomerEnv
import monarch.http.routes.CustomerRoutes
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.implicits._
import org.http4s.server.Router
import zio._
import zio.interop.catz._

object Server {
  def run(): ZIO[BootEnv, Throwable, Unit] = for {
    config <- ZIO.service[HttpServerConfig]
    routes = Router(
      "/" -> (CustomerRoutes.routes <+> CustomerRoutes.swaggerRoutes)
    ).orNotFound
    _ <- BlazeServerBuilder[RIO[CustomerEnv, *]]
      .bindHttp(config.port, config.host)
      .withoutBanner
      .withHttpApp(routes)
      .serve
      .compile
      .drain
  } yield ()
}
