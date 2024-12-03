package flex.client

import flex.network.IpAddr
import kotlinx.coroutines.channels.Channel

// Scans for Flex servers in the LAN and sends results to a channel
class ServerScanner(val ip: IpAddr, val ch: Channel<Array<Int>>) {
	fun start() {
		val static = ip.bytes.slice(0..3)
		// this one will now launch a coroutine which checks all IP addresses similar to the given one
		// I feel some very big pain incoming managing this coroutine and channel stuff
	}
}