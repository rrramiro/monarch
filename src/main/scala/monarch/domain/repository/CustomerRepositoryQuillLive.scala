package monarch.domain.repository

import monarch.domain.models.Customer
import zio._

import javax.sql.DataSource

final case class CustomerRepositoryQuillLive(dataSource: DataSource)
    extends CustomerRepository {
  private val env: ULayer[DataSource] = ZLayer.fromZIO(ZIO.succeed(dataSource))

  override def getById(id: Long): Task[Option[Customer]] =
    SqlContext.getById(id).provide(env).orDie

  override def insert(customer: Customer): Task[Long] =
    SqlContext.insert(customer).provide(env).orDie

  override def update(id: Long, customer: Customer): Task[Unit] =
    SqlContext.update(id, customer).provide(env).orDie
}

object CustomerRepositoryQuillLive {
  val layer: URLayer[DataSource, CustomerRepositoryQuillLive] =
    ZLayer.fromZIO(
      ZIO.service[DataSource].map(CustomerRepositoryQuillLive(_))
    )
}
