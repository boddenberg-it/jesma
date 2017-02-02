#!/usr/bin/groovy
/**
*
*/
class AndroidDeviceHandlerFoo {

  List androidDevices
  List serials
  List previousSerials

// maybe parse State directly to avoid shitload of adb devices calls.... (yeah)
  List readSerials() {

    boolean offsetReached = false;

    List serials = []
    List adbDevices = "adb devices".execute().text.split('\n')

    adbDevices.each {
      if (offsetReached) {
        serials.add(it.split('\t')[0])
      } else if (it.equals("List of devices attached ")) {
        offsetReached = true
      }
    }

    serials
  }

void updateSerials() {
  this.previousSerials = this.serials
  this.serials = readSerials()
}

void init() {
  this.serials.each() {
    createAndroidSlave(it)
  }
}

void createAndroidSlave(){
  // get specific information from fetched config File
  // instanciate AndroidDevice + its inner JenkinsSlave object and that's should it be.
  // Then every round only all AndroidDevices has to be checked according to JSON configuration!
}

void checkDeviceConnections() {

  updateSerials()

  if (!previousSerials) { init(); return }

  // check existing slave instances
  androidDevices.each() { -> androidDevice
    if (!serials.contains(androidDevice.serial)) {
      androidDevice.slave.kill()
      androidDevices.removeAll {
        it.serial.equals(androidDevice.serial)
      }
    }
  }

  // check if new AndroidDevices must be added, because new device has been added
  serials.each() {
    if (!this.previousSerials.contains(it)) { createAndroidSlave(it) }
  }
}

}




}

def foo = new AndroidDeviceHandlerFoo()


foo.updateSerials()
println foo.serials != null
println foo.previousSerials
foo.updateSerials()
println foo.serials
println foo.previousSerials


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
