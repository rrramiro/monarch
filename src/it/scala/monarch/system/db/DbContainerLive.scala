package monarch.system.db

import com.dimafeng.testcontainers.PostgreSQLContainer
import monarch.system.db.HikariConfig
import org.testcontainers.utility.DockerImageName
import zio._

object DbContainerLive {
  def databaseImage(
      imageName: String = "postgres:12-alpine"
  ): ULayer[DockerImageName] = ZLayer.succeed(DockerImageName.parse(imageName))

  val layer: URLayer[DockerImageName, HikariConfig] =
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
            new HikariConfig {
              setDriverClassName(classOf[org.postgresql.Driver].getName)
              setJdbcUrl(container.jdbcUrl)
              setUsername(container.username)
              setPassword(container.password)
            }
          )
        )
    }
}
