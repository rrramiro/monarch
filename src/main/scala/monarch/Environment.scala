package monarch

import monarch.domain.repository.CustomerRepository
import monarch.domain.service.CustomerService
import monarch.system.config.DatabaseConfig
import monarch.system.config.HttpServerConfig

import javax.sql.DataSource

object Environment {
  type CustomerEnv = CustomerService with CustomerRepository
  type ServerEnv = CustomerEnv with HttpServerConfig with DatabaseConfig
  type BootEnv = ServerEnv with DataSource
}
