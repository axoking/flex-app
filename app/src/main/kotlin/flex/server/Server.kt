package flex.server

import io.ktor.server.engine.*
import io.ktor.server.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.netty.*
import flex.api.*

class Server {
	var isPushing = false
	var isPulling = false

	val ktorServer = embeddedServer(Netty, port = 56789) {
		routing {
			post("/api") {
				//handleApiRequest(call)
				call.respondText("Uninmplemented!")
			}
			get("/api") {
				call.respondText("Uninmplemented!")
			}

			get("/") {
				handleWebview(call)
			}
		}
	}

	fun start(wait: Boolean) {
		println("Server running")
		ktorServer.start(wait = wait)
	}

	suspend fun handleWebview(call: RoutingCall) {
		call.response.header("Content-Type", "text/html")
		call.respondText(renderHtml(
			"webview",
			mapOf("key" to "12345", "pushHidden" to "", "pullHidden" to "hidden", "pushFiles" to "lol")
		))
	}

	suspend fun handleApiRequest(call: RoutingCall) {
		val args = decodeArgs(call.receiveText())

		val handler = when (args["action"]) {
			"hello" -> ApiHandle::greet
			else -> ApiHandle::unknownAction
		}
		handler(call, args)
	}
}
