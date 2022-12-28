package monarch.http

import eu.timepit.refined.types.string.NonEmptyString
import monarch.domain.models.Customer

object data {

  trait CustomerInfo {
    val firstName: NonEmptyString
    val lastName: NonEmptyString

    def toDomain: Customer = Customer.create(firstName, lastName)
  }

  final case class CustomerData(
      id: Long,
      firstName: NonEmptyString,
      lastName: NonEmptyString
  ) extends CustomerInfo

  final case class CreateCustomerData(
      firstName: NonEmptyString,
      lastName: NonEmptyString
  ) extends CustomerInfo

  final case class UpdateCustomerData(
      firstName: NonEmptyString,
      lastName: NonEmptyString
  ) extends CustomerInfo

  implicit class CustomerWrapper(c: Customer) {
    def toData: CustomerData = CustomerData(c.id, c.firstName, c.lastName)
  }

}
