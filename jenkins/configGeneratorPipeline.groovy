/* JSM configuration updater */
def savedConfig = getConfig().trim()
def latestConfig = createConfig().trim()

if (!savedConfig.equals(latestConfig)) {
  log.info("[INFO] JSM configuration file has been updated")
  updateConfig(latestConfig)
}

// functions
String createConfig() {
  String configJson = "{ \"jsm_clients\": "

	hudson.model.Hudson.instance.slaves.each() {

		configJson += """{
                "name": "${it.name}",
                "mode": "${it.mode}",
                "remoteFS":  "${it.remoteFS}",
                "executors": "${it.numExecutors}",
                "jnlp_secret": "${it.getComputer().getJnlpMac()}",
                "jsmc_json": ${it.nodeProperties.envVars.jsmc_json[0]}
        },""".stripIndent()
  }
  configJson = configJson.substring(0, (configJson.length() - 1)) + "}"
}

void updateConfig(String config) {
  def printWriter = new PrintWriter("/var/jenkins_home/userContent/jsmConfig.json")
	printWriter.println(config)
	printWriter.close()
}

String getConfig(){
  String fileContents = new File('/var/jenkins_home/userContent/jsmConfig.json').text
}

String encrypt(String input) {
  input.tbc...
}
