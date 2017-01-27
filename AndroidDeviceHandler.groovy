#!/usr/bin/groovy
/**
*
*/

class AndroidDeviceHandlerFoo {

  List AndroidDevices
  List serials
  List previousSerials

// maybe parse State directly to avoid shitload of adb devices calls.... (yeah)
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

void updateSerials() {
  this.previousSerials = this.serials
  this.serials = getSerials()
}

}


/*
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
      androidHandler.isDeviceUsable(it) ) {
      androidHelper.AndroidDevices.add(new AndroidDevice(serial: it)
  }
}
*/

// run check, so all added slaves will start
// ### lifecycle end :)
