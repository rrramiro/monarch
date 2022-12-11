package monarch.system.config

case class DatabaseConfig(
    className: String,
    url: String,
    user: String,
    password: String
)
