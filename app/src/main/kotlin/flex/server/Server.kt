package flex.server

import flex.Log
import io.ktor.server.engine.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.netty.*
import java.io.File

const val MAX_PUSH_FILES = 5

class Server {
	private var isPushing = false
	private var isPulling = false
	private var pushFiles: List<String> = listOf()

	private val ktorServer = embeddedServer(Netty, port = 56789) {
		routing {
			get("/") {
				handleWebview(call)
			}

			get("/download") {
				handleDownload(call)
			}
		}
	}

	fun start(wait: Boolean) {
		Log.info("HTTP server running")
		ktorServer.start(wait = wait)
	}

	fun enablePush(files: List<String>) {
		if (files.size > MAX_PUSH_FILES) {
			throw IllegalArgumentException("A maxmium of $MAX_PUSH_FILES is allowed to push, ${files.size} given")
		}
		isPushing = true
		pushFiles = files
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
		val file= File(pushFiles[index!!])
		call.response.header("Content-Disposition", "attachment; filename=${file.name}")
		call.respondFile(file)
	}

	private fun generatePushFilesHtml(): String  = pushFiles.withIndex().joinToString("\n") {
		val escaped = escapeHtml(File(it.value).name)
		"<li><a href=\"download?i=${it.index}\">$escaped</a></li>"
	}
}
