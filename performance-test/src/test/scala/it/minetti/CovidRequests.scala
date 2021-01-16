package it.minetti

import io.gatling.core.Predef._
import io.gatling.core.feeder.Feeder
import io.gatling.core.scenario._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._
import scala.util.Random

class CovidRequests extends Simulation {

  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl("https://covid-sentry.ddns.net")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val regionCodes: Seq[String] = Seq(
    "01", "02", "08", "05", "07", "10"
  )

  val generator: Feeder[Any] = Iterator.continually(
    Map("RegionCode" -> regionCodes(Random.nextInt(regionCodes.length)))
  )

  val scn: ScenarioBuilder = scenario("Backend requests")
    .feed(generator)
    .exec(http("UI Home")
      .get("/"))
    .pause(50.milliseconds) // Note that Gatling has recorder real time pauses
    .exec(http("API national")
      .get("/api/data/national"))
    .pause(1)
    .exec(http("UI region")
      .get("/region"))
    .pause(300.milliseconds)
    .exec(http("API region random code 1")
      .get("/api/data/regions/${RegionCode}"))
    .pause(2)
    .exec(http("API region code 06")
      .get("/api/data/regions/06"))
    .pause(2)
    .exec(http("API region random code 2")
      .get("/api/data/regions/${RegionCode}"))
    .pause(2)
    .exec(http("UI images")
      .get("/images"))
    .pause(734.milliseconds)
    .exec(http("API images")
      .get("/api/images"))
    .pause(734.milliseconds)
    .exec(http("UI about")
      .get("/about"))

  setUp(scn.inject(
    rampUsers(10).during(5.seconds),
    nothingFor(500.milliseconds),
    constantUsersPerSec(20).during(10.seconds),
    rampUsersPerSec(10).to(20).during(5.seconds).randomized,
    heavisideUsers(500).during(5.seconds),
    constantUsersPerSec(2).during(5.seconds)
  ).protocols(httpProtocol)).assertions(
    forAll.responseTime.percentile(95).lt(2000),
    forAll.successfulRequests.percent.gt(95),
    global.failedRequests.percent.lt(5)
  )
}