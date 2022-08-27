package monarch.http

trait ErrorInfo extends Serializable
object ErrorInfo {
  case class NotFound(msg: String) extends ErrorInfo

  case class BadRequest(msg: String, errors: List[String] = List.empty)
      extends ErrorInfo

  case class InternalServerError(msg: String) extends ErrorInfo

}
