package Hue

import org.json4s._
import org.json4s.native.Serialization.{read, write}
import Hue.Models._

object Bridge {
  def apply(user: User) = {
    implicit val formats = DefaultFormats

    val response = new HttpWrapper("https://www.meethue.com").get("/api/nupnp")

    read[List[BridgeInformation]](response) match {
      case List(x) => new Bridge(x, user)
      case Nil     => throw new Exception("couldn't fetch bridge configuration")
    }
  }
}

class Bridge(val info: BridgeInformation, val user: User) {
  implicit val formats = DefaultFormats

  val httpWrapper = new HttpWrapper("http://" + info.internalipaddress)

  val urlPrefix = "/api/" + user.username

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
      val response = httpWrapper.post("/api", write(user))
      parseHueResponse[HueSuccess[User]](response)
    }

    def getConfiguration() = {
      val response = httpWrapper.get(urlPrefix + "/config")
      parseHueResponse[UserConfiguration](response)
    }

    def modifyConfiguration(conf: UserConfiguration) = {
      val response = httpWrapper.put(urlPrefix + "/config", write(conf))
      parseHueResponse[HueSuccess[UserConfiguration]](response)  // TODO - not sure this is actually the response
    }

    def deleteUser(deleteUserName: String) = {
      val response = httpWrapper.delete(urlPrefix + "/config/whitelist/" + deleteUserName)
      parseHueResponse[HueSuccess[String]](response)
    }

    def getFullState() = {
      val response = httpWrapper.get(urlPrefix)
      parseHueResponse[FullState](response)
    }
  }

  /**
   * Given a response from the Hue bridge, attempt to parse the
   * JSON into the given type parameter case class.
   *
   * If an exception is caught during the JSON read, then an
   * attempt will be made to read out a Hue error message.  If
   * that fails, then an exception will be thrown.
   */
  protected def parseHueResponse[T: Manifest](response: String) =
    try {
      read[T](response)
    } catch {
      case e: Exception => read[HueError](response)
    }
}