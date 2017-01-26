#!/usr/bin/groovy
/**
*
*/

class AndroidDeviceHandler {

  String jenkinsHost

  List AndroidDevices
  List serials
  List previousSerials


boolean isDeviceUsable(String serial) {
  "adb devices".execute().text.split("${serial}\t")[1].trim().equals("device")
}

void updateSerials() {
  this.previousSerials = this.serials
  this.serials = getSerials()
}

List getSerials() {

  List serials
  List adbDevices = "adb devices".execute().text.split('\n')

  adbDevices.each {

    if (offsetReached) {
      it = it.split('\t')
      serials.add(it[0])
    }

    if (!offsetReached && it.equals("List of devices attached ")) {
      offsetReached = true
    }
  }

  serials
  }

}

// ## lifecycle
// ## init
def androidHandler = new AndroidHandler(jenkinsHost: "", )

// ## routine
androidHandler.updateSerials()
// check for devicesless slaves
androidHandler.previousSerials.each() {
  if(!androidHandler.serials.contains(it)) {
    androidHelper.killOrpanedSlave(it)
  }
}
// check for new slavesless devices
androidHandler.serials.each() {
  if( !androidHandler.previousSrials.contains(it) &&
      androidHandler.isDeviceUsable(it) )
    {
      androidHelper.AndroidDevices.add(new AndroidDevice(serial: it)
    }
  }

// run check, so all added slaves will start
// ### lifecycle end :)


List getAdbClients(){

  List adbClients = []
  Boolean offsetReached = false;

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
    println it.isAirplaneModeOn()
    println it.getSSID()
    println it.isAirplaneModeOn()

}
