#!/usr/bin/groovy
/**
*
*/


List getAdbClients(){

  List adbClients = []
  Boolean offsetReached = false;

  List adbDevices = "adb devices".execute().text.split('\n')

  adbDevices.each {
    if (offsetReached) {
      // transformation from String to List
      it = it.split('\t')
      println it
      adbClients.add(new AndroidDevice(serial: it[0], adbState: it[1]))
    }
    // trigger if
    if (!offsetReached && it.equals("List of devices attached ")) {
      offsetReached = true
    }
  }
  return adbClients
}

getAdbClients().each {

    println it.serial
    println it.isCharging()
    println it.getBatteryCapacity()
    println it.isAirplaneModeOn()
    println it.getSSID()
    println it.isAirplaneModeOn()

}
