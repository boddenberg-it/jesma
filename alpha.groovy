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

  //def myBookList = [myBook,myBook2,myBook3]

  //def jsonBuilder = new groovy.json.JsonBuilder()
  //jsonBuilder(devices: adbClients)

  //println jsonBuilder
  return adbClients
}

def test = getAdbClients()
// that's it :)
println test[0].state
println test[0].serial
