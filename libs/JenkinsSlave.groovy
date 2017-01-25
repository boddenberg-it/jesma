#!/usr/bin/groovy
import groovy.json.JsonSlurper

class JenkinsSlave {

  private final String jenkinsHost
  private final String jnlpUrl
  private final String slaveName
  private final String secret
  private final String serial

  private UNIXProcess process

  boolean startSlave() {
    // sanity check whether log dir is accessible
    //assertTrue new File(./slaveLogs).isAccessible()

    Date date = new Date().format('yyyy-MM-dd_hh:mm:ss')

    this.process = """
      java -jar slave.jar
         -slaveLog ./logs/slaves/${date}_${this.slaveName}.${this.serial}.log
         -jnlpUrl ${this.jnlpUrl}
         -secret ${this.secret}
    """.stripIndent().execute()

    sleep(3000)
    this.isOnline()
  }

  boolean killSlave() {
    this.process.destroy()
    sleep(3000)
    !this.isOnline()
  }

  // is'es methods
  boolean isOnline() {
    // this.getJson("offline").offline one line, if getJson works :D
    String url = "${this.jenkinsHost}/computer/${this.slavName}/api/json?tree=offline"
    def jsonResp = new JsonSlurper().parseText(url.toURL().getText())
    !jsonResp.offline
  }


  // returns null (as String) or the reason why user disconnected slave
  String isManuallyDisconnected() {
    String url = "${this.jenkinsHost}/computer/${this.slavName}/api/json?tree=offlineCause[description]"
    def jsonResp = new JsonSlurper().parseText(url.toURL().getText())
    if(!jsonResp.offlineCause) { return null }
    jsonResp.offlineCause.description
  }

  boolean isBuilding() {
    // this.getJson("executors[idle]").idle one line, if getJson works :D
    String url = "${this.jenkinsHost}/computer/${this.slavName}/api/json?tree=executors[idle]"
    def jsonResp = new JsonSlurper().parseText(url.toURL().getText())
    jsonResp.idle
  }

  // have to try it, but will make the two methods smaller!
  def getJson(String query) {
    String url = "${this.jenkinsHost}/computer/${this.slavName}/api/json?tree=${query}"
    new JsonSlurper().parseText(url.toURL().getText())
  }

  // setter methods
  void setJenkinsHost(String host) {
    this.jenkinsHost = host
  }

  void setJnlpUrl(String jnlpUrl) {
    this.jnlpUrl = jnlpUrl
  }

  void setSlaveName(String serial) {
    this.serial = serial
  }

  void setSecret(String secret) {
    this.secret = secret
  }

  void setSerial(String serial) {
    this.serial = serial
  }

}
