package flex

import flex.server.Server

fun main() {
	val server = Server()
	server.enablePush(listOf("src/main/resources/test_files/hello.txt"))
	server.start(true)
}
