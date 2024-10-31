package forex.services.rates

import cats.Applicative
import interpreters._

import forex.config.OneFrameConfig
import forex.cache.CacheProvider.RatesCache

object Interpreters {
  def live[F[_]: Applicative](config: OneFrameConfig, ratesCache:RatesCache ): Algebra[F] = new OneFrameClient[F](config, ratesCache)
}
