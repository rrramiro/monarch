package monarch.system.db

import com.dimafeng.testcontainers.PostgreSQLContainer
import monarch.system.config.DatabaseConfig
import org.testcontainers.utility.DockerImageName
import zio._

object DbContainerLive {
  def databaseImage(
      imageName: String = "postgres:12-alpine"
  ): ULayer[DockerImageName] = ZLayer.succeed(DockerImageName.parse(imageName))

  val layer: URLayer[DockerImageName, DatabaseConfig] =
    ZLayer.scopedEnvironment {
      ZIO
        .service[DockerImageName]
        .flatMap(dockerImageName =>
          ZIO.acquireRelease(
            ZIO.attempt(PostgreSQLContainer.Def(dockerImageName).start()).orDie
          )(container => ZIO.attempt(container.stop()).ignoreLogged)
        )
        .map(container =>
          ZEnvironment(
            DatabaseConfig(
              className = classOf[org.postgresql.Driver].getName,
              url = container.jdbcUrl,
              user = container.username,
              password = container.password
            )
          )
        )
    }
}
