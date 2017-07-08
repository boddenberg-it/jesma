#!/usr/bin/groovy

class AndroidDevice {
  // RaspberryPi only
  int powerGpio
  JenkinsSlave slave
  String serial
  String adbShellState // probably not hold at all

  /***** Android  ******/

  String adbShell(String command) {
    "adb -s ${this.serial} shell ${command}".execute().text.trim()
  }

  boolean isAirplaneModeOn() {
    adbShell("settings get global airplane_mode_on").equals("1")
  }

  boolean isDeviceUsable(String serial) {
    "adb devices".execute().text.split("${this.serial}\t")[1]
      .trim().equals("device")
  }

  boolean isWiFiConnected() {
    try { adbShell("ip addr").split("wlan")[1].split("inet")[1].length() }
    catch (ArrayIndexOutOfBoundsException) { return false }
    true
  }

  int getBatteryCapacity() {
    Integer.parseInt(adbShell("cat /sys/class/power_supply/battery/capacity"))
  }

  String getSSID() {
     adbShell("dumpsys netstats").split("networkId=\"")[1].split("\"")[0]
  }

  void toggleAirplaneMode() {
    adbShell("am start -a android.settings.AIRPLANE_MODE_SETTINGS"); sleep(1000)
    adbShell("input keyevent 19"); sleep(1000)
    adbShell("input keyevent 23"); sleep(100)
		adbShell("am force-stop com.android.settings")
  }


  // unfinished methods
  int getBrightness() {
      adbShell("dumpsys power") // add something like "grep Display Power"
  }

  List ping(String address) {
      pingOutput = adbShell(" ping -c 5 ${address}").split('\n')
  }

}
