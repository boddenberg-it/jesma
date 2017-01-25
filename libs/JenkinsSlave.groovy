#!/usr/bin/groovy
import groovy.json.JsonSlurper

class JenkinsSlave {

  String jnlpUrl
  String serial
  String slaveApiUrl
  String slaveName

  UNIXProcess process

  // extract method! into own class Jenkins slave
  /***** Jenkins slave methods****/
  boolean startSlave() {
    // sanity check whether log dir is accessible
    //assertTrue new File(./slaveLogs).isAccessible()
    Date date = new Date().format('yyyy-MM-dd_hh:mm:ss')

    this.process = """
                     java -jar slave.jar
                        -slaveLog ./slaveLogs/${date}_${slaveName}.${serial}.log
                        -jnlpUrl ${jnlpUrl}
                        -secret
                   """.stripIndent().execute()

    sleep(5000)
    this.isOnline()
  }

  boolean killSlave() {
    this.process.destroy()
    sleep(5000)
    !this.isOnline()
  }

  boolean isOnline() {
    String url = "${jenkinsHost}/computer/${this.slavName}/api/json?tree=offline"
    def jsonResp = new JsonSlurper().parseText(url.toURL().getText())
    !jsonResp.offline
  }

}
