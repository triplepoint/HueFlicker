package Hue

object Models {
  case class User(username: String, devicetype: String)
  case class BridgeInformation(id: String, internalipaddress: String, macaddress: String, name: String)

  case class Light()

  // Payload Objects
  case class UserConfiguration(proxyport: Int, UTC: String, name: String) // TODO fill these out

  // Container Objects
  type HueSuccess[T] = List[HueSuccessObject[T]]
  case class HueSuccessObject[T](success: T)
  type HueError = List[HueErrorObject]
  case class HueErrorObject(error: HueErrorDetail)
  case class HueErrorDetail(`type`: Int, address: String, description: String)

  case class FullState(lights: AnyRef, groups: AnyRef, config: Config, schedules: AnyRef)
  case class Config(name: String, mac: String, dhcp: Boolean, ipaddress: String, netmask: String, gateway: String, proxyaddress: String, proxyport: Int, UTC: String, whitelist: AnyRef, swversion: String, swupdate: AnyRef, linkbutton: Boolean, portalservices: Boolean)
}
