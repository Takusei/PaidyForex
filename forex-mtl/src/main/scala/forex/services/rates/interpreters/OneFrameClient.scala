package forex.services.rates.interpreters

import forex.services.rates.Algebra
import cats.Applicative
import cats.syntax.applicative._
import cats.syntax.either._
import forex.domain.{ Price, Rate, Timestamp }
import forex.services.rates.Errors._

import sttp.client4.quick._
import sttp.client4.Response

import io.circe.parser
import forex.config.OneFrameConfig

import forex.logger.Logger.logger
import forex.cache.CacheProvider.RatesCache

class OneFrameClient[F[_]: Applicative](config: OneFrameConfig, ratesCache: RatesCache) extends Algebra[F] {

  override def get(pair: Rate.Pair): F[Error Either Rate] = {
    logger.info(s"OneFrameClient.get called with pair ${pair.toString()}")

    ratesCache.getIfPresent(pair.toString) match {
      case Some(rate) =>
        logger.info(s"Rate for pair $pair found in cache")
        rate.asRight[Error].pure[F]
      case None =>
        logger.warn(s"Rate for pair $pair not found in cache, will fetch from OneFrame API")
        getRateFromOneFrame(pair)
    }
  }

  /**
    * Fetches the rate for a given pair from the OneFrame API
    * and caches it in the rates cache
    *
    * @param pair
    * @return the rate for the given pair or an error
    */
  def getRateFromOneFrame(pair: Rate.Pair): F[Error Either Rate] = {
    val response: Response[String] = quickRequest
      .get(uri"${config.uri}/rates?pair=${pair.from.toString}${pair.to.toString}")
      .header("token", config.token)
      .send()

    logger.info(s"Request to OneFrame API for pair $pair returned status code ${response.code}")

    val parsedResponse = parser.parse(response.body)
    logger.info(s"Parsed response: $parsedResponse")

    // Assuming the response is a JSON array with a single object
    val price: Double = parsedResponse match {
      case Right(json) =>
        json.asArray.flatMap(_.headOption) match {
          case Some(firstObject) =>
            firstObject.hcursor.downField("price").as[Double] match {
              case Right(value) => value
              case Left(_)      => 0.0
            }
          case None => 0.0
        }
      case Left(_) => 0.0
    }

    logger.info(s"Successfully parsed price $price from response for pair $pair")

    val currentRate = Rate(pair, Price(price), Timestamp.now)
    ratesCache.put(pair.toString, currentRate)
    currentRate.asRight[Error].pure[F]
  }
}
