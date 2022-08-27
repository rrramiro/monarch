package monarch

import monarch.domain.repository.CustomerRepositoryQuillLive
import monarch.domain.service.CustomerServiceLive
import monarch.http.Server
import monarch.system.config.ConfigurationLive
import monarch.system.db.FlywayAdapter
import monarch.system.db.FlywayAdapterLive
import monarch.system.db.DatasourceLive
import monarch.Environment.BootEnv
import zio._

object Boot extends ZIOApp {

  override type Environment = BootEnv

  override val environmentTag: EnvironmentTag[Environment] =
    EnvironmentTag[Environment]

  override val bootstrap: ZLayer[Scope, Any, Environment] =
    ZLayer.makeSome[Scope, BootEnv](
      ConfigurationLive.layer,
      FlywayAdapterLive.layer,
      DatasourceLive.layer,
      CustomerRepositoryQuillLive.layer,
      CustomerServiceLive.layer
    )

  override def run: ZIO[Environment with Scope, Any, Any] =
    (FlywayAdapter.migrate() *> Server.run())
      .tapError(err => ZIO.logError(err.getMessage))
      .exitCode
}
