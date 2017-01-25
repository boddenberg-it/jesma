#!/usr/bin/groovy
class AndroidDevice {
  String state
  String serial

  int getBatteryCapacity() {
    return Integer.parseInt("adb -s ${this.serial} shell cat /sys/class/power_supply/battery/capacity"
      .execute().text.trim())
  }

  String isCharging(){
    return "adb -s ${this.serial} shell cat /sys/class/power_supply/battery/status"
      .execute().text.trim()
  }
}
