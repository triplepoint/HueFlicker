package Hue

import scalaj.http.{HttpOptions, Http}

class HttpWrapper(domain: String) {

  def get(route: String) =
    makeRequestandExtractResponse(Http(getApiRouteUrl(route)).method("GET"))

  def post(route: String, data: String) =
    makeRequestandExtractResponse(Http.postData(getApiRouteUrl(route), data))

  def put(route: String, data: String) =
    makeRequestandExtractResponse(Http.postData(getApiRouteUrl(route), data).method("PUT"))

  def delete(route: String) =
    makeRequestandExtractResponse(Http(getApiRouteUrl(route)).method("DELETE"))

  protected def getApiRouteUrl(route: String) =
    s"$domain/api/$route".stripSuffix("/")

  protected def makeRequestandExtractResponse(request: Http.Request) =
    request
      .option(HttpOptions.connTimeout(1000))
      .option(HttpOptions.readTimeout(5000))
      .asString
}