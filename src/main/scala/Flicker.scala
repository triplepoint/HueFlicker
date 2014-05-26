import Hue._

object Flicker extends App {
  val bridge = Bridge("triplepoint")
  println("bridge information: ", bridge.info)

  val createUserResponse = bridge.configuration.createNewUser()
  println(createUserResponse)
}