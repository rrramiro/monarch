package monarch

import monarch.system.config.Configuration
import monarch.domain.service.CustomerService
import monarch.domain.repository.CustomerRepository
import monarch.system.db.FlywayAdapter

object Environment {
  type CustomerEnv = CustomerService with CustomerRepository
  type ServerEnv = CustomerEnv with Configuration
  type BootEnv = ServerEnv with FlywayAdapter
}
