package monarch

import monarch.Environment.BootEnv
import monarch.domain.repository.CustomerRepositoryQuillLive
import monarch.domain.service.CustomerServiceLive
import monarch.http.Server
import monarch.system.config.ConfigurationLive
import monarch.system.db.DatasourceLive
import monarch.system.db.FlywayAdapter
import zio._

object Boot extends ZIOApp {

  override type Environment = BootEnv

  override lazy val environmentTag: EnvironmentTag[Environment] =
    EnvironmentTag[Environment]

  override lazy val bootstrap = ZLayer.make[BootEnv](
    ConfigurationLive.layer,
    DatasourceLive.layer,
    CustomerRepositoryQuillLive.layer,
    CustomerServiceLive.layer,
    Scope.default
  )

  override def run: ZIO[Environment with ZIOAppArgs with Scope, Any, Any] =
    (FlywayAdapter.migrate() *> Server.run())
      .tapError(err => ZIO.logError(err.getMessage))
      .exitCode
}
