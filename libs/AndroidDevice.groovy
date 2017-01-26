#!/usr/bin/groovy

class AndroidDevice {
  // RaspberryPi only
  int powerGpio
  JenkinsSlave slave
  String serial

  /***** Android  ******/
  int getBatteryCapacity() {
    return Integer.parseInt(this.adb("cat /sys/class/power_supply/battery/capacity"))
  }

  boolean isCharging(){
    String output = this.adb("cat /sys/class/power_supply/battery/status")
    output.equals("Charging") || output.equals("Full")
  }

  boolean isAirplaneModeOn() {
    this.adb("settings get global airplane_mode_on").equals("1")
  }

  void toggleAirplaneMode() {
    this.adb("am start -a android.settings.AIRPLANE_MODE_SETTINGS")
    sleep(1000)
    this.adb("input keyevent 19")
    sleep(1000)
    this.adb("input keyevent 23")
    sleep(100)
		this.adb("am force-stop com.android.settings")
  }

  String getSSID() {
    String output = this.adb("dumpsys netstats").split("networkId=\"")[1]
    output.split("\"")[0]
  }

  String adb(String command) {
    "adb -s ${this.serial} shell ${command}".execute().text.trim()
  }

  // above methods are "tested"

  // rooted would could use these things:
  // adbShell shell su -c 'svc wifi disable'

  // TODO: focus on the jenkins slave!

    boolean toggleCharging() {

    }

    boolean gotInetConnection() {
        this.ping()
    }

    boolean ping(String address) {
      List pingOutput = this.adb(" ping -c 5 ${address}").split('\n')
      return true
    }
}
