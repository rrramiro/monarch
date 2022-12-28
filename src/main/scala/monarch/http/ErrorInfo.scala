package monarch.http

trait ErrorInfo extends Serializable
object ErrorInfo {
  final case class NotFound(msg: String) extends ErrorInfo

  final case class BadRequest(msg: String, errors: List[String] = List.empty)
      extends ErrorInfo

  final case class InternalServerError(msg: String) extends ErrorInfo

}
