package flex.server

import flex.Log
import io.ktor.server.engine.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.isRegularFile

const val MAX_PUSH_FILES = 5

class Server {
	var isPushing = false
	var isPulling = false
	var pushFiles: MutableList<String> = mutableListOf()
		private set

	private val ktorServer = embeddedServer(Netty, port = 56789) {
		routing {
			get("/") {
				handleWebview(call)
			}

			get("/download") {
				handleDownload(call)
			}

			post("/pullrq") {
				handlePullRequest(call)
			}
		}
	}

	fun start(wait: Boolean) {
		Log.info("HTTP server running")
		ktorServer.start(wait = wait)
	}

	fun addPushFile(path: String) {
		if (pushFiles.size + 1 > MAX_PUSH_FILES) {
			throw IllegalStateException("A maxmium of $MAX_PUSH_FILES is allowed to push")
		}
		else if (!Path(path).isRegularFile()) {
			throw IllegalArgumentException("Invalid path given")
		}
		else {
			pushFiles.add(path)
		}
	}

	private suspend fun handleWebview(call: RoutingCall) {
		call.response.header("Content-Type", "text/html")
		call.respondText(renderHtml("webview", mapOf(
			"pushHidden" to if (isPushing) "" else "hidden",
			"pullHidden" to if (isPulling) "" else "hidden",
			"pushFiles" to generatePushFilesHtml()
		)))
	}

	private suspend fun handleDownload(call: RoutingCall) {
		val index = call.queryParameters["i"]?.toInt()
		if (index == null || index >= pushFiles.size || index < 0) call.respond(400)
		val file = File(pushFiles[index!!])
		call.response.header("Content-Disposition", "attachment; filename=${file.name}")
		call.respondFile(file)
	}

	private suspend fun handlePullRequest(call: RoutingCall) {
		val args = call.receiveParameters()
		val size = args["size"]?.toInt()
		val name = args["name"]

		Log.debug("File $name of size $size requested")
	}

	private fun generatePushFilesHtml(): String  = pushFiles.withIndex().joinToString("\n") {
		val escaped = escapeHtml(File(it.value).name)
		"<li><a href=\"download?i=${it.index}\">$escaped</a></li>"
	}
}
