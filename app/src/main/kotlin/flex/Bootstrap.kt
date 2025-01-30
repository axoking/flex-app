package flex

import flex.cli.CLI
import flex.server.Server
import flex.values.VERSION
import java.nio.file.Paths

fun main() {
	/*Log.setup()
	Log.info("Welcome to FLEX $VERSION :)\n")
	val dir = Paths.get("").toAbsolutePath()
	Log.debug("The working directory is $dir")*/

	/*val server = Server()
	server.enablePush(listOf("src/main/resources/test_files/hello.txt"))
	server.start(true)*/

	CLI().run()
}