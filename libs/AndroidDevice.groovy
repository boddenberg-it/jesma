#!/usr/bin/groovy

class AndroidDevice {
  // RaspberryPi only
  int powerGpio

  String serial
  String state

  String slaveName
  String jnlpUrl
  UNIXProcess process


  // extract method! into own class Jenkins slave
  /***** Jenkins slave methods****/
  UNIXProcess startSlave() {
    // sanity check whether log dir is accessible
    assertTrue new File(./slaveLogs).isAccessible()

    return """java -jar slave.jar
                   -slaveLog ./slaveLogs/date.${slaveName}.${serial}.log
                   -jnlpUrl ${jnlpUrl}
                   -secret""".execute()
    // process.destroy() <- to kill!
  }

  void killSlave() {
    this.process.destroy()
  }

  /***** Android  ******/
  int getBatteryCapacity() {
    return Integer.parseInt("adb -s ${this.serial} shell cat /sys/class/power_supply/battery/capacity"
      .execute().text.trim())
  }

  String isCharging(){
    return "adb -s ${this.serial} shell cat /sys/class/power_supply/battery/status"
      .execute().text.trim()
  }

  boolean pingGoogle() {

  }

  boolean isAirplaneModeOn() {

  }

  boolean setAirplaneMode() {

  }
}
