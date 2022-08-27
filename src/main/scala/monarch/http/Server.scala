package monarch.http

import cats.syntax.all._
import monarch.system.config.Configuration
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.implicits._
import zio._
import zio.interop.catz._
import org.http4s.server.Router
import monarch.http.routes.CustomerRoutes
import monarch.Environment.{ServerEnv, CustomerEnv}
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import sttp.tapir.server.http4s.ztapir.ZHttp4sServerInterpreter

object Server {
  def run(): ZIO[ServerEnv, Throwable, Unit] = for {
    config <- ZIO.service[Configuration]
    swaggerRoutes = ZHttp4sServerInterpreter()
      .from(
        SwaggerInterpreter().fromServerEndpoints[RIO[CustomerEnv, *]](
          CustomerRoutes.endpoints,
          "Monarch",
          "0.1.0"
        )
      )
      .toRoutes
    routes = Router(
      "/" -> (CustomerRoutes.routes <+> swaggerRoutes)
    ).orNotFound
    _ <- BlazeServerBuilder[RIO[CustomerEnv, *]]
      .bindHttp(config.httpServer.port, config.httpServer.host)
      .withoutBanner
      .withHttpApp(routes)
      .serve
      .compile
      .drain
  } yield ()
}
