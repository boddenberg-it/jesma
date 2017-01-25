#!/usr/bin/groovy

class AndroidDevice {
  // RaspberryPi only
  int powerGpio

  String serial
  String state

  String slaveName
  String jnlpUrl
  UNIXProcess process




  /***** Android  ******/
  int getBatteryCapacity() {
    return Integer.parseInt("adb -s ${this.serial} shell cat /sys/class/power_supply/battery/capacity"
      .execute().text.trim())
  }

  String isCharging(){
    return "adb -s ${this.serial} shell cat /sys/class/power_supply/battery/status"
      .execute().text.trim()
  }

  boolean isConnectedViaWiFi() {

  }

  boolean isConnectedToWWW() {

  }

  boolean ping(String address) {

    List pingOutput = "adb -s ${this.serial} shell ping -c 5 ${address}"
      .execute().text.trim().split('\n')


    return true
  }

  boolean isAirplaneModeOn() {
    def aiplaneMode = Integer.valueOf(
      "adb -s ${this.serial} shell settings get global airplane_mode_on"
      .execute().text.trim())

    airplaneMode > 0
  }

  boolean setAirplaneMode() {

  }

  boolean setWifiMode() {

  }

  String getSSID(){
    def output = "adb shell dumpsys netstats | grep -E 'iface=wlan.*networkId'"
      .execute().text.trim()

    output.substring()
  }

  // rooted would could use these things:
  // adb shell su -c 'svc wifi disable'
}
