package Hue

import scalaj.http.{HttpOptions, Http}
import org.json4s._
import org.json4s.native.JsonMethods._

case class ErrorDescription(`type`: Int, address: String, description: String)

class HttpWrapper(domain: String) {

  // This is necessary, for the JSON4s decoding below
  implicit val formats = DefaultFormats

  def get[T: Manifest](route: String) =
    makeRequestandExtractResponse(Http(getApiRouteUrl(route)).method("GET"))

  def post[T: Manifest](route: String, data: String) =
    makeRequestandExtractResponse(Http.postData(getApiRouteUrl(route), data))

  def put[T: Manifest](route: String, data: String) =
    makeRequestandExtractResponse(Http.postData(getApiRouteUrl(route), data).method("PUT"))

  def delete[T: Manifest](route: String) =
    makeRequestandExtractResponse(Http(getApiRouteUrl(route)).method("DELETE"))

  protected def getApiRouteUrl(route: String) =
    s"$domain/api/$route".stripSuffix("/")

  protected def makeRequestandExtractResponse[T: Manifest](request: Http.Request) = {
    val responseBody = request
      .option(HttpOptions.connTimeout(1000))
      .option(HttpOptions.readTimeout(5000))
      .asString

    println("parsed json: ", responseBody)

    val json = parse(responseBody)

    json.extractOpt[List[T]] match {
      case Some(x) => x.head
      case None    => (json \\ "success").extractOpt[T] match {
        case Some(x) => x
        case None    => (json \\ "error").extractOpt[ErrorDescription] match {
          case Some(x) => throw new RuntimeException("API Error: " + x.description)
          case None    => throw new RuntimeException("API response content did not parse")
        }
      }
    }
  }
}