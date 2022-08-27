package monarch.domain

import eu.timepit.refined.types.string.NonEmptyString

object models {

  final case class Customer(
      id: Long,
      firstName: NonEmptyString,
      lastName: NonEmptyString
  )

  object Customer {
    def create(
        firstName: NonEmptyString,
        lastName: NonEmptyString
    ): Customer = Customer(-1, firstName, lastName)
  }
}
