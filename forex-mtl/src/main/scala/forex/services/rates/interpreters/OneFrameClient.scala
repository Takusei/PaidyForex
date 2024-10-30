package forex.services.rates.interpreters

import forex.services.rates.Algebra
import cats.Applicative
import cats.syntax.applicative._
import cats.syntax.either._
import forex.domain.{ Price, Rate, Timestamp }
import forex.services.rates.errors._

import sttp.client4.quick._
import sttp.client4.Response

import io.circe.parser
import forex.config.OneFrameConfig

class OneFrameClient[F[_]: Applicative](config: OneFrameConfig) extends Algebra[F] {

  override def get(pair: Rate.Pair): F[Error Either Rate] = {
    println("OneFrameClient.get pair " + pair)

    val response: Response[String] = quickRequest
      .get(uri"${config.uri}/rates?pair=${pair.from.toString}${pair.to.toString}")
      .header("token", config.token)
      .send()

    println("response.code " + response.code)
    // prints: 200
    
    val parsedResponse = parser.parse(response.body)
    println(parsedResponse)

    // Assuming the response is a JSON array with a single object
    val price: Double = parsedResponse match {
      case Right(json) =>
        json.asArray.flatMap(_.headOption) match {
          case Some(firstObject) =>
            firstObject.hcursor.downField("price").as[Double] match {
              case Right(value) => value
              case Left(_) => 0.0
            }
          case None => 0.0
        }
      case Left(_) => 0.0
    }

    Rate(pair, Price(price), Timestamp.now).asRight[Error].pure[F]
  }
}
