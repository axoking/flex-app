package flex.network

import java.net.InetAddress

class IpAddr(b: Array<UByte>) {
	val bytes: Array<UByte> = b

	init {
		if (b.size != 4) {
			throw IllegalArgumentException("Invalid bytearray given for IP address")
		}
	}

	companion object {
		fun local(): IpAddr {
			val addr = InetAddress.getLocalHost()
			return IpAddr(addr.address.map { it.toUByte() }.toTypedArray())
		}
	}

	override fun toString(): String = bytes.joinToString(".")
}