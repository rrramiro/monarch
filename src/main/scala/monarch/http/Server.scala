package monarch.http

import cats.syntax.all._
import monarch.Environment.CustomerEnv
import monarch.Environment.ServerEnv
import monarch.http.routes.CustomerRoutes
import monarch.system.config.HttpServerConfig
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.implicits._
import org.http4s.server.Router
import sttp.tapir.server.http4s.ztapir.ZHttp4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import zio._
import zio.interop.catz._

object Server {
  def run(): ZIO[ServerEnv, Throwable, Unit] = for {
    config <- ZIO.service[HttpServerConfig]
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
      .bindHttp(config.port, config.host)
      .withoutBanner
      .withHttpApp(routes)
      .serve
      .compile
      .drain
  } yield ()
}
