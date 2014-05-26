package Hue

case class BridgeInformation(id: String, internalipaddress: String, macaddress: String)

case class UsernameCreateResponse(username: String)
case class ConfigurationGetResponse(username: String) // @TODO fix this
case class ConfigurationModifyResponse(username: String) // @TODO fix this
case class CompleteStateGetResponse(username: String) // @TODO fix this

object Bridge {
  def apply(username: String) = {
    val httpWrapper = new HttpWrapper("https://www.meethue.com")
    val bridgeInformation = httpWrapper.get[BridgeInformation]("nupnp")
    new Bridge(bridgeInformation, username)
  }
}

class Bridge(val info: BridgeInformation, val username: String) {
  val httpWrapper = new HttpWrapper("http://" + info.internalipaddress)

  object lights {
    def getAllLights() = {}
    def getNewLights() = {}
    def searchForNewLights() = {}
    def getLightAttributesAndState() = {}
    def setLightAttributes() = {}
    def setLightState() = {}
  }

  object groups {
    def getAllGroups() = {}
    def createGroup() = {}
    def getGroupAttributes() = {}
    def SetGroupAttributes() = {}
    def setGroupState() = {}
    def deleteGroup() = {}
  }

  object schedules {
    def getAllSchedules() = {}
    def createSchedule() = {}
    def getScheduleAttributes() = {}
    def setScheduleAttributes() = {}
    def deleteSchedule() = {}
  }

  object configuration {
    def createNewUser() = {
      val payload = s"""{"devicetype":"test user","username":"$username"}"""
      httpWrapper.post[UsernameCreateResponse]("", payload)
    }

    def getConfiguration() =
      httpWrapper.get[ConfigurationGetResponse](s"$username/config")

    def modifyConfiguration() = {
      val payload = "" // @TODO s"""{"devicetype":"test user","username":"$username"}"""
      httpWrapper.post[ConfigurationModifyResponse](s"$username/config", payload)
    }

    def deleteUser(deleteUser: String) =
      httpWrapper.delete[String](s"$username/config/whitelist/$deleteUser")

    def getFullState() =
      httpWrapper.get[CompleteStateGetResponse](username)
  }
}