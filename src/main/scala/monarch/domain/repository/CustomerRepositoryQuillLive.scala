package monarch.domain.repository

import monarch.domain.models.Customer
import eu.timepit.refined.types.string.NonEmptyString
import eu.timepit.refined.api._
import io.getquill._
import zio._

import java.sql.SQLException
import javax.sql.DataSource

case class CustomerRepositoryQuillLive(dataSource: DataSource)
    extends CustomerRepository {
  val env: ULayer[DataSource] = ZLayer.fromZIO(ZIO.succeed(dataSource))

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

object SqlContext extends PostgresZioJdbcContext(SnakeCase) {
  inline given InsertMeta[Customer] = insertMeta(_.id)

  inline given UpdateMeta[Customer] = updateMeta(_.id)

  implicit val nonEmptyStringMappedDecode
      : MappedEncoding[NonEmptyString, String] = MappedEncoding(
    _.value
  )

  implicit val nonEmptyStringMappedEncode
      : MappedEncoding[String, NonEmptyString] = MappedEncoding(
    RefType.applyRef[NonEmptyString].unsafeFrom(_)
  )

  inline def customerTable: Quoted[EntityQuery[Customer]] = quote {
    querySchema[Customer](
      "customers",
      _.id -> "id",
      _.firstName -> "first_name",
      _.lastName -> "last_name"
    )
  }

  def getById(id: Long): ZIO[DataSource, SQLException, Option[Customer]] =
    run(
      quote(
        customerTable.filter(_.id == lift(id))
      )
    ).map(_.headOption)

  def insert(customer: Customer): ZIO[DataSource, SQLException, Long] =
    run(
      quote(
        customerTable.insertValue(lift(customer))
      )
    )

  def update(
      id: Long,
      customer: Customer
  ): ZIO[DataSource, SQLException, Unit] = run(
    quote(
      customerTable.filter(_.id == lift(id)).updateValue(lift(customer))
    )
  ).unit
}
