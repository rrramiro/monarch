package monarch

import eu.timepit.refined.auto._
import monarch.Environment.CustomerEnv
import monarch.domain.repository.CustomerRepositoryQuillLive
import monarch.domain.service.CustomerServiceLive
import monarch.http.data.CreateCustomerData
import monarch.http.routes.CustomerRoutes
import monarch.system.db._
import org.http4s.HttpApp
import org.http4s.server._
import sttp.tapir.DecodeResult
import sttp.tapir.PublicEndpoint
import sttp.tapir.client.http4s.Http4sClientInterpreter
import sttp.tapir.server.interceptor.decodefailure.DefaultDecodeFailureHandler.FailureMessages
import zio._
import zio.interop.catz._
import zio.test.Assertion._
import zio.test._

object HttpSpec extends ZIOSpecDefault {

  val http4sRoutes: HttpApp[RIO[CustomerEnv, *]] = Router(
    "/" -> CustomerRoutes.routes
  ).orNotFound

  val http4sClientInterpreter = Http4sClientInterpreter[RIO[CustomerEnv, *]]()
  private def decodeFailure[T](decodeResult: DecodeResult[T]): Task[T] =
    decodeResult match {
      case failure: DecodeResult.Failure =>
        ZIO.fail(
          new Exception(
            FailureMessages.failureDetailMessage(failure).getOrElse("error")
          )
        )
      case DecodeResult.Value(v) => ZIO.succeed(v)
    }

  private def toRequest[I, E, O, R](
      e: PublicEndpoint[I, E, O, R]
  ): I => ZIO[CustomerEnv, Throwable, Either[E, O]] = { input: I =>
    val request = http4sClientInterpreter.toRequest(e, baseUri = None)
    val (userRequest, parseResponse) = request(input)
    http4sRoutes.run(userRequest).flatMap(parseResponse).flatMap(decodeFailure)
  }

  val getCustomerRequest = toRequest(CustomerRoutes.getCustomer)
  val createCustomerRequest = toRequest(CustomerRoutes.createCustomer)
  val updateCustomerRequest = toRequest(CustomerRoutes.updateCustomer)

  def spec: Spec[TestEnvironment with Scope, Any] =
    (suite("HttpSpec")(
      test("createCustomer") {
        val createCustomerData = CreateCustomerData(
          firstName = "firstName",
          lastName = "lastName"
        )
        for {
          result <- createCustomerRequest(createCustomerData)
        } yield {
          assert(result)(isRight(equalTo(1L)))
        }
      }
    ) @@ TestAspect.beforeAll(FlywayAdapter.migrate())).provideShared(
      DbContainerLive.databaseImage(),
      DbContainerLive.layer,
      DatasourceLive.layer,
      Scope.default,
      CustomerRepositoryQuillLive.layer,
      CustomerServiceLive.layer
    )
}
