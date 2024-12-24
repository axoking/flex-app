package flex.server

import io.ktor.server.engine.*
import io.ktor.server.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.netty.*
import flex.api.*
import kotlin.random.Random

const val MAX_PUSH_FILES = 5
const val KEY_BYTES = 32

class Server {
	var isPushing = false
	var isPulling = false
	var pushFiles: List<String> = listOf()

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

	fun enablePush(files: List<String>) {
		if (files.size > MAX_PUSH_FILES) {
			throw IllegalArgumentException("A maxmium of $MAX_PUSH_FILES is allowed to push, ${files.size} given")
		}
		isPushing = true
		pushFiles = files
	}

	suspend fun handleWebview(call: RoutingCall) {
		call.response.header("Content-Type", "text/html")
		val text = "Here's some text: 1 < 3, \"hello world!\" & happy new year >>>>>"
		call.respondText(renderHtml("webview", mapOf(
			"pushHidden" to if (isPushing) "" else "hidden",
			"pullHidden" to if (isPulling) "" else "hidden",
			"pushFiles" to generatePushFilesHtml()
		)))
	}

	fun generatePushFilesHtml(): String  = pushFiles.joinToString("\n") {
		val escaped = escapeHtml(it)
		"<li>$escaped</li>"
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
