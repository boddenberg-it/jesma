String configJson = "{ \"jsm_clients\": [\n"

//Node node = jenkins.model.Jenkins.instance.slaves.find { it.name == "jsmc_testSlave" }

for ( node in jenkins.model.Jenkins.instance.slaves) {

props = node.nodeProperties.envVars

configJson += """{
                "name": "${node.name}",
                "mode": "${node.mode}",
                "remoteFS":  "${node.remoteFS}",
                "executors": "${node.numExecutors}",
                "jnlp_secret": "${node.getComputer().getJnlpMac()}",
		"adb_serial": "${props.ANDROID_SERIAL[0]}",
                "jsmc_json": ${props.jsmc_json[0]}
              },""".stripIndent()
}
// close JSON
configJson = configJson.substring(0, configJson.lastIndexOf(','))
configJson += "\n]}"
println configJson
