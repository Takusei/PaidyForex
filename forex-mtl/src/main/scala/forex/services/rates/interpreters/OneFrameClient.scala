package forex.services.rates.interpreters

import forex.services.rates.Algebra
import cats.Applicative
import cats.syntax.applicative._
import cats.syntax.either._
import forex.domain.{ Price, Rate, Timestamp }
import forex.services.rates.errors._

import sttp.client4.quick._
import sttp.client4.Response

class OneFrameClient[F[_]: Applicative] extends Algebra[F] {

  override def get(pair: Rate.Pair): F[Error Either Rate] = {
    println("OneFrameClient.get")
    val response: Response[String] = quickRequest
      .get(uri"http://localhost:8080/rates?pair=USDJPY")
      .header("token", "10dc303535874aeccc86a8251e6992f5")
      .send()

    println(response.code)
    // prints: 200

    println(response.body)
    // prints some JSON string

    // Assuming you want to parse the response and return a Rate
    // This part will depend on how you want to handle the response
    // For now, let's assume a successful response returns a dummy Rate
    Rate(pair, Price(BigDecimal(100)), Timestamp.now).asRight[Error].pure[F]
  }

}
