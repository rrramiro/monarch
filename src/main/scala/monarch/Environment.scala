package monarch

import monarch.domain.repository.CustomerRepository
import monarch.domain.service.CustomerService
import monarch.http.HttpServerConfig

import javax.sql.DataSource

object Environment {
  type CustomerEnv = CustomerService with CustomerRepository
  type ServiceEnv = CustomerEnv with DataSource
  type BootEnv = ServiceEnv with HttpServerConfig
}
