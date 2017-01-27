List getAdbClients() {

  List adbClients = []
  Boolean offsetReached = false

  List adbDevices = "adb devices".execute().text.split('\n')

  adbDevices.each {
    if (offsetReached) {
      it = it.split('\t')
      adbClients.add(new AndroidDevice(serial: it[0], adbState: it[1]))
    }
    if (!offsetReached && it.equals("List of devices attached ")) {
      offsetReached = true
    }
  }

  adbClients
}

getAdbClients().each {

    println it.serial
    println it.isCharging()
    println it.getBatteryCapacity()
    println it.getSSID()
    println it.isAirplaneModeOn()
//    println "toggleAirplaneMode"
//    it.toggleAirplaneMode()
//    println it.isAirplaneModeOn()
//    println it.getSSID()
    println it.isWiFiConnected()
    println it.isDeviceUsable()
}