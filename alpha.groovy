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
      it = it.split('\t')
      adbClients.add(new AndroidDevice(serial: it[0], state: it[1]))
    }
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

}
