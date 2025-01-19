package flex

import flex.server.Server
import mu.KotlinLogging
import flex.values.VERSION
import java.nio.file.Paths

fun main() {
	val logger = KotlinLogging.logger {}
	logger.info("Welcome to FLEX $VERSION :)")
	logger.debug {
		val dir = Paths.get("").toAbsolutePath()
		"The working directory is $dir"
	}

	val server = Server()
	server.enablePush(listOf("src/main/resources/test_files/hello.txt"))
	server.start(true)
}
