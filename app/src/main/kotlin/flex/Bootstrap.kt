package flex;

import flex.server.Server
import flex.client.Client
import kotlinx.coroutines.*

fun main() {
	runBlocking {
		launch {
			Server().start()
		}

		launch {
			println("Client is launched in 2 seconds")
			delay(2000L)
			val con = Client("localhost")
			con.greet()
		}
	}

	println("* done *")
}
