package flex

import flex.server.Server

fun main() {
	val server = Server()
	server.enablePush(listOf("sui.txt", "deinemutteristdeinvater", "a<b", "hallo welt"))
	server.start(true)
}
