package monarch.http.routes

import io.circe.generic.auto._
import io.circe.refined._
import monarch.Environment.CustomerEnv
import monarch.domain.service.CustomerService
import monarch.http.ErrorInfo
import monarch.http.ErrorInfo._
import monarch.http.data._
import org.http4s.HttpRoutes
import sttp.model.StatusCode
import sttp.tapir.EndpointOutput.OneOf
import sttp.tapir.PublicEndpoint
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._
import sttp.tapir.server.http4s.ztapir.ZHttp4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import sttp.tapir.ztapir._
import zio._

object CustomerRoutes {

  val httpErrors: OneOf[ErrorInfo, ErrorInfo] = oneOf[ErrorInfo](
    oneOfVariant(StatusCode.InternalServerError, jsonBody[InternalServerError]),
    oneOfVariant(StatusCode.BadRequest, jsonBody[BadRequest]),
    oneOfVariant(StatusCode.NotFound, jsonBody[NotFound])
  )

  val getCustomer: PublicEndpoint[Long, ErrorInfo, CustomerData, Any] =
    endpoint.get
      .in("customers" / path[Long]("id"))
      .out(jsonBody[CustomerData])
      .errorOut(httpErrors)

  val createCustomer: PublicEndpoint[CreateCustomerData, ErrorInfo, Long, Any] =
    endpoint.post
      .in("customers")
      .in(jsonBody[CreateCustomerData])
      .out(plainBody[Long])
      .errorOut(httpErrors)

  val updateCustomer
      : PublicEndpoint[(Long, UpdateCustomerData), ErrorInfo, Unit, Any] =
    endpoint.put
      .in("customers" / path[Long]("id"))
      .in(jsonBody[UpdateCustomerData])
      .errorOut(httpErrors)
      .out(statusCode(StatusCode.Accepted))

  def getCustomerEndpoint: ZServerEndpoint[CustomerEnv, Any] =
    getCustomer.zServerLogic { (id: Long) =>
      CustomerService.get(id).mapBoth(e => NotFound(e.getMessage), _.toData)
    }

  def createCustomerEndpoint: ZServerEndpoint[CustomerEnv, Any] =
    createCustomer.zServerLogic { (data: CreateCustomerData) =>
      CustomerService
        .create(data.toDomain)
        .mapError(e => BadRequest(e.getMessage))
    }

  def updateCustomerEndpoint: ZServerEndpoint[CustomerEnv, Any] =
    updateCustomer.zServerLogic { case (id: Long, data: UpdateCustomerData) =>
      CustomerService
        .update(id, data.toDomain)
        .mapError(e => BadRequest(e.getMessage))
    }

  val endpoints: List[ZServerEndpoint[CustomerEnv, Any]] = List(
    getCustomerEndpoint,
    createCustomerEndpoint,
    updateCustomerEndpoint
  )

  val swaggerEndpoints: List[ZServerEndpoint[CustomerEnv, Any]] =
    SwaggerInterpreter().fromServerEndpoints(endpoints, "Monarch", "0.1.0")

  val routes: HttpRoutes[RIO[CustomerEnv, *]] =
    ZHttp4sServerInterpreter()
      .from(
        endpoints ++ swaggerEndpoints
      )
      .toRoutes
}
