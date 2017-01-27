#!/usr/bin/groovy

class AndroidDevice {
  // RaspberryPi only
  int powerGpio
  JenkinsSlave slave
  String serial
  String adbState // probably not hold at all

  /***** Android  ******/

  String adb(String command) {
    "adb -s ${this.serial} shell ${command}".execute().text.trim()
  }

  boolean isAirplaneModeOn() {
    this.adb("settings get global airplane_mode_on").equals("1")
  }

  boolean isCharging(){
    String output = this.adb("cat /sys/class/power_supply/battery/status")
    output.equals("Charging") || output.equals("Full")
  }

  boolean isDeviceUsable(String serial) {
  "adb devices".execute().text.split("${this.serial}\t")[1].trim().equals("device")
  }

  boolean isWiFiConnected() {
    try { this.adb("ip addr").split("wlan")[1].split("inet")[1].length() }
    catch (ArrayIndexOutOfBoundsException) { return false }
    true
  }

  int getBatteryCapacity() {
    Integer.parseInt(this.adb("cat /sys/class/power_supply/battery/capacity"))
  }

  String getSSID() {
     this.adb("dumpsys netstats").split("networkId=\"")[1].split("\"")[0]
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


  // unfinished methods
  void tooglePowerSupply() {

  }

  List ping(String address) {
      List pingOutput = this.adb(" ping -c 5 ${address}").split('\n')
      true
    }
}
