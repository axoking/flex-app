package flex.client

import flex.network.IpAddr
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import kotlinx.coroutines.channels.Channel

// Scans for Flex servers in the LAN and sends results to a channel
class ServerScanner(val clientIp: IpAddr, val ch: Channel<IpAddr>) {
	val con = HttpClient(CIO)

	suspend fun run() {
		val (a, b, c, _) = clientIp.bytes;
		// this one will now launch a coroutine which checks all IP addresses similar to the given one
		// I feel some very big pain incoming managing this coroutine and channel stuff

		for (d in 0..10) {
			val ip = IpAddr(arrayOf(a, b, c, d.toUByte()))

			if (ip == clientIp) {
				continue
			}

			try {
				con.get("http://$ip:5678/api")
			}
			catch (e: Exception) {
				println("Exception occurred: $e")
			}
		}
	}
}