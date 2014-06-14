package Hue

import org.json4s._
import org.json4s.native.Serialization.{read, write}
import Hue.Bridge._

case class BridgeInformation(id: String, internalipaddress: String, macaddress: String, name: String)

object Bridge {

   // Payload Objects
  case class User(username: String, devicetype: Option[String])
  case class UserConfiguration(proxyport: Int, UTC: String, name: String) // TODO fill these out

  // Containers
  type HueSuccess[T] = List[HueSuccessObject[T]]
  case class HueSuccessObject[T](success: T)
  type HueError = List[HueErrorObject]
  case class HueErrorObject(error: HueErrorDetail)
  case class HueErrorDetail(`type`: Int, address: String, description: String)

  def apply(username: String) = {
    implicit val formats = DefaultFormats

    val httpWrapper = new HttpWrapper("https://www.meethue.com")
    val response = httpWrapper.get("nupnp")
    read[List[BridgeInformation]](response) match {
      case List(x) => new Bridge(x, username)
      case Nil     => throw new Exception("broke")
    }
  }
}

class Bridge(val info: BridgeInformation, val username: String) {

  val httpWrapper = new HttpWrapper("http://" + info.internalipaddress)

  implicit val formats = DefaultFormats

//  implicit val formats = Serialization.formats(NoTypeHints)
//
//  object lights {
//    def getAllLights() = {}
//    def getNewLights() = {}
//    def searchForNewLights() = {}
//    def getLightAttributesAndState() = {}
//    def setLightAttributes() = {}
//    def setLightState() = {}
//  }
//
//  object groups {
//    def getAllGroups() = {}
//    def createGroup() = {}
//    def getGroupAttributes() = {}
//    def SetGroupAttributes() = {}
//    def setGroupState() = {}
//    def deleteGroup() = {}
//  }
//
//  object schedules {
//    def getAllSchedules() = {}
//    def createSchedule() = {}
//    def getScheduleAttributes() = {}
//    def setScheduleAttributes() = {}
//    def deleteSchedule() = {}
//  }
//
  object configuration {
    def createNewUser() = {
      val body = User(username, Some("test user"))
      val response = httpWrapper.post("", write(body))
      parseResult[HueSuccess[User]](response)
    }

    def getConfiguration() = {
      val response = httpWrapper.get(s"$username/config")
      parseResult[UserConfiguration](response)
    }

    def modifyConfiguration(proxyport: Int, UTC: String, name: String) = {
      val body = UserConfiguration(proxyport, UTC, name)
      val response = httpWrapper.put(s"$username/config", write(body))
      parseResult[HueSuccess[UserConfiguration]](response)
    }

//    def deleteUser(deleteUser: String) =
//      httpWrapper.delete[String](s"$username/config/whitelist/$deleteUser")
//
//    def getFullState() =
//      httpWrapper.get[CompleteStateGetResponse](username)
  }

  protected def parseResult[T: Manifest](response: String) = {
    implicit val formats = DefaultFormats

    try {
      read[T](response)
    } catch {
      case e: Exception => read[HueError](response)
    }
  }
}