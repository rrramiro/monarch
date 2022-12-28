package monarch.domain.repository

import eu.timepit.refined.api.RefType
import eu.timepit.refined.types.string.NonEmptyString
import io.getquill.PostgresZioJdbcContext
import io.getquill.SnakeCase
import monarch.domain.models.Customer
import zio.ZIO

import java.sql.SQLException
import javax.sql.DataSource

object SqlContext extends PostgresZioJdbcContext(SnakeCase) {
  implicit val customerInsertMeta = insertMeta[Customer](_.id)

  implicit val customerUpdateMeta = updateMeta[Customer](_.id)

  implicit val nonEmptyStringMappedDecode
      : MappedEncoding[NonEmptyString, String] = MappedEncoding(
    _.value
  )

  implicit val nonEmptyStringMappedEncode
      : MappedEncoding[String, NonEmptyString] = MappedEncoding(
    RefType.applyRef[NonEmptyString].unsafeFrom(_)
  )

  val customerTable = quote {
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
