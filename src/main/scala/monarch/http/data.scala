package monarch.http

import monarch.domain.models.Customer
import eu.timepit.refined.types.string.NonEmptyString

object data {

  trait CustomerInfo {
    val firstName: NonEmptyString
    val lastName: NonEmptyString

    def toDomain: Customer = Customer.create(firstName, lastName)
  }

  case class CustomerData(
      id: Long,
      firstName: NonEmptyString,
      lastName: NonEmptyString
  ) extends CustomerInfo

  case class CreateCustomerData(
      firstName: NonEmptyString,
      lastName: NonEmptyString
  ) extends CustomerInfo

  case class UpdateCustomerData(
      firstName: NonEmptyString,
      lastName: NonEmptyString
  ) extends CustomerInfo

  implicit class CustomerWrapper(c: Customer) {
    def toData: CustomerData = CustomerData(c.id, c.firstName, c.lastName)
  }

}
