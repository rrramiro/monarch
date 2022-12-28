package monarch.system.db

import com.typesafe.config.Config

import java.util.Properties
import scala.jdk.CollectionConverters._

object HikariConfig {
  @SuppressWarnings(Array("org.wartremover.warts.ToString"))
  def apply(conf: Config): HikariConfig = new HikariConfig({
    val p = new Properties
    for (entry <- conf.entrySet.asScala) {
      p.setProperty(entry.getKey, entry.getValue.unwrapped.toString)
    }
    p
  })
}
