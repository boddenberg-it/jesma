String configJson = "{ \"jsm_clients\": "

Node node = Jenkins.instance.slaves.find { it.name == "jsmc_testSlave" }


configJson += """{
                "name": "${node.name}",
				"host": "host...Ahaaa",
                "mode": "${node.mode}",
                "remoteFS":  "${node.remoteFS}",
                "executors": "${node.numExecutors}",
                "jnlp_secret": "${node.getComputer().getJnlpMac()}",
                "jsmc_json": ${node.nodeProperties.envVars.jsmc_json[0]}
			  },""".stripIndent()

// close JSON
configJson = configJson.substring(0,configJson.lastIndexOf(','))
configJson += "}"

/*
this is how the "jsmc_json" "environment variable"
looks like in the "Node Properties" of the slave ->

{"AndroidDevice": {"name": "jsmc_testSlave"}}

pretty much JSON :)

*/
