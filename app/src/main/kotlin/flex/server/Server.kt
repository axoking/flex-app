package flex.server

import io.ktor.server.engine.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.netty.*
import flex.api.*
import flex.server.ApiHandle

class Server {
	val ktorServer = embeddedServer(Netty, port = 56789) {
		routing {
			post("/api") {
				handleApiRequest(call)
			}
			get("/hello") {
				call.respondText("einmal Döner mit alles bidde")
			}
		}
	}

	fun start() {
		println("Server running")
		ktorServer.start(wait = false)
	}

	suspend fun handleApiRequest(call: RoutingCall) {
		val args = decodeArgs(call.receiveText())
		val version = args["version"]

		val handler = when (args["action"]) {
			"hello" -> ApiHandle::greet
			else -> ApiHandle::unknownAction
		}
		handler(call, args)
	}
}
