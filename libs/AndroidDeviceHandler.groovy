#!/usr/bin/groovy
/**
*
*/
class AndroidDeviceHandler {

  List androidDevices
  List serials
  List previousSerials

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
    previousSerials = serials
    serials = readSerials()
  }

  void init() {
    serials.each() { createAndroidSlave(it) }
  }

  void createAndroidSlave(){
    // get specific information from fetched config File
    // instanciate AndroidDevice + its inner JenkinsSlave object and that's should it be.
    // Then every round only all AndroidDevices has to be checked according to JSON configuration!
  }

  void updateDeviceConnections() {

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
    // create AndroidSlave for newly connected devices
    serials.each() {
      if (!this.previousSerials.contains(it)) { createAndroidSlave(it) }
    }
  }

}

/* testing within class (simple invoke this groovy file)
def foo = new AndroidDeviceHandler()
prinltn
foo.updateSerials()
println foo.serials != null
println foo.previousSerials
foo.updateSerials()
println foo.serials
println foo.previousSerials
*/
