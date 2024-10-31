package forex.logger

import ch.qos.logback.classic.{ Logger => LogbackLogger }
import org.slf4j.LoggerFactory

object Logger {
  val logger: LogbackLogger = LoggerFactory.getLogger(this.getClass).asInstanceOf[LogbackLogger]
}