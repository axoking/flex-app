package flex

import flex.server.Server
import flex.client.Client
import flex.client.ServerScanner
import flex.network.IpAddr
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun main() {
	val scanner = ServerScanner(IpAddr.local(), Channel())

	runBlocking {
		launch { scanner.run() }
	}
}
