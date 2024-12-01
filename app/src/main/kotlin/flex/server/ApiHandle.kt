package flex.server

import io.ktor.server.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import flex.api.*

suspend fun handleApi(call: RoutingCall) {
	val args = decodeArgs(call.receiveText())
	val version = args["version"]

	when (args["action"]) {
		"hello" -> call.respondText("Hello, you're using version $version")
		else -> call.respondText("Unknown action.")
	}

}
