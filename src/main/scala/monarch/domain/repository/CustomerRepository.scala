package monarch.domain.repository

import monarch.domain.models.Customer
import zio._

trait CustomerRepository {
  def getById(id: Long): Task[Option[Customer]]

  def insert(customer: Customer): Task[Long]

  def update(id: Long, customer: Customer): Task[Unit]
}
