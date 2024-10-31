package forex.cache

import com.github.blemale.scaffeine.{ Cache, Scaffeine }
import forex.domain.Rate
import scala.concurrent.duration._

object CacheProvider {
  type RatesCache = Cache[String, Rate]

  val ratesCache: RatesCache = Scaffeine()
    .maximumSize(10000)
    .expireAfterWrite(2.minutes)
    .build[String, Rate]()
}
