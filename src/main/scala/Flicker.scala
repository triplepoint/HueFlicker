import Hue._
import Hue.Models._

object Flicker extends App {
  val user = User("triplepoint", "test user")
  val bridge = Bridge(user)
  //println("bridge information: ", bridge.info)

  //val response = bridge.configuration.createNewUser()
  val response = bridge.configuration.getFullState()
  println(response)
}