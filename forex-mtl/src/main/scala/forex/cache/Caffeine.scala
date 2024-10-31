package forex.cache

import com.github.blemale.scaffeine.{ Cache, Scaffeine }
import forex.domain.Rate
import scala.concurrent.duration._

/**
  * CacheProvider is a singleton object that provides a cache for storing rates.
  * The cache is implemented using Scaffeine, a Scala-friendly wrapper around Caffeine.
  * The cache is a simple key-value store where the key is a string and the value is a Rate.
  * The cache has a maximum size of 10,000 entries and each entry expires after 2 minutes.
  */
object CacheProvider {
  type RatesCache = Cache[String, Rate]

  val ratesCache: RatesCache = Scaffeine()
    .maximumSize(10000)
    .expireAfterWrite(2.minutes)
    .build[String, Rate]()
}
