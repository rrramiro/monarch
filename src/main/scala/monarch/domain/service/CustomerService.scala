package monarch.domain.service

import monarch.domain.errors.DomainError
import monarch.domain.models.Customer
import monarch.domain.repository.CustomerRepository
import zio._

trait CustomerService {
  def get(id: Long): Task[Customer]

  def create(customer: Customer): Task[Long]

  def update(id: Long, customer: Customer): Task[Unit]
}

object CustomerService {
  def get(id: Long) = ZIO.serviceWithZIO[CustomerService](_.get(id))

  def create(customer: Customer) =
    ZIO.serviceWithZIO[CustomerService](_.create(customer))

  def update(id: Long, customer: Customer) =
    ZIO.serviceWithZIO[CustomerService](_.update(id, customer))
}

case class CustomerServiceLive(repo: CustomerRepository)
    extends CustomerService {
  override def get(id: Long): Task[Customer] =
    repo
      .getById(id)
      .flatMap(maybeCustomer =>
        ZIO
          .fromOption(maybeCustomer)
          .mapError(_ => DomainError.CustomerNotFound(id))
      )

  override def create(customer: Customer): Task[Long] =
    repo.insert(customer).mapError(_ => DomainError.UnknownError)

  override def update(
      id: Long,
      customer: Customer
  ): Task[Unit] =
    for {
      c <- get(id)
      _ <- repo
        .update(
          id,
          c.copy(firstName = customer.firstName, lastName = customer.lastName)
        )
        .mapError(_ => DomainError.UnknownError)
    } yield ()
}

object CustomerServiceLive {
  val layer: URLayer[CustomerRepository, CustomerService] =
    ZLayer.fromZIO(ZIO.serviceWith(CustomerServiceLive(_)))
}
