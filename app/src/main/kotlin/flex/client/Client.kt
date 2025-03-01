package flex.client

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import flex.api.*
import flex.values.*

suspend fun HttpResponse.readArgs() = decodeArgs(this.bodyAsText())

// Client which is bound to a specific server
class Client(host: String) {
	val con = HttpClient(CIO)
	val url: String = "http://$host:56789/api"

	suspend fun greet() {
		val resp = requestApi(
			"action" to "hello",
			"version" to VERSION,
		)
		val args = resp.readArgs()
		val version = args["version"]
		println("Server uses version $version")
	}

	suspend fun requestApi(vararg args: Pair<String, String>) = con.post(url) {
		setBody(encodeArgs(args.toMap()))
	}
}
