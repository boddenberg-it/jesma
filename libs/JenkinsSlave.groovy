#!/usr/bin/groovy
import groovy.json.JsonSlurper

class JenkinsSlave {

  String hostUrl  // config (setup jsm_host)
  String name     // userContent (dynamically via Jenkins job)
  String secret   // userContent (dynamically via Jenkins job)

  private UNIXProcess process

  boolean start() {
    // sanity check whether log dir is accessible
    //assertTrue new File(./slaveLogs).isAccessible()

    Date date = new Date().format('yyyy-MM-dd_hh:mm:ss')

    this.process = """
      java -Dhudson.remoting.Launcher.pingIntervalSec=-1
        -jar slave.jar
        -slaveLog ./logs/slaves/${date}_${this.name}.${this.serial}.log
        -jnlpUrl "${this.hostUrl}/computer/${this.name}/slave-agent.jnlp
        -secret ${this.secret}
    """.stripIndent().execute()

    sleep(3000)
    this.isOnline()
  }

  boolean kill() {
    this.process.destroy()
    sleep(3000)
    !this.isOnline()
  }

  // is'es methods
  boolean isOnline() {
    this.getJson("offline").offline
  }


  // returns null (as String) or the reason why user disconnected slave
  String isManuallyDisconnected() {
    def jsonResp = this.getJson("offlineCause[description]")
    if(!jsonResp.offlineCause) { return null }
    jsonResp.offlineCause.description
  }

  boolean isBuilding() {
    this.getJson("executors[idle]").idle
  }

  def getJson(String query) {
    String url = "${this.hostUrl}/computer/${this.name}/api/json?tree=${query}"
    new JsonSlurper().parseText(url.toURL().getText())
  }

}
