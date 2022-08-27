package monarch.domain

import scala.util.control.NoStackTrace

object errors {

  sealed trait Error extends Throwable with NoStackTrace

  sealed abstract class DomainError(msg: String)
      extends Throwable(msg)
      with Error
  object DomainError {
    case class CustomerNotFound(customerId: Long)
        extends DomainError(s"Customer with id ${customerId} was not found!")
    case object UnknownError extends DomainError(s"Unkown Error!")

  }

}
