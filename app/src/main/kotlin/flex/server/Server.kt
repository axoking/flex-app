package flex.server

import io.ktor.server.engine.*
import io.ktor.server.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.netty.*
import flex.api.*
import io.ktor.client.engine.*
import mu.KotlinLogging
import java.io.File
import kotlin.io.path.Path

const val MAX_PUSH_FILES = 5

class Server {
	var isPushing = false
	var isPulling = false
	var pushFiles: List<String> = listOf()

	val ktorServer = embeddedServer(Netty, port = 56789) {
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
		KotlinLogging.logger{}.info("HTTP server running")
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
		call.respondText(renderHtml("webview", mapOf(
			"pushHidden" to if (isPushing) "" else "hidden",
			"pullHidden" to if (isPulling) "" else "hidden",
			"pushFiles" to generatePushFilesHtml()
		)))
	}

	suspend fun handleDownload(call: RoutingCall) {
		val index = call.queryParameters["i"]?.toInt()
		if (index == null || index >= pushFiles.size || index < 0) call.respond(400)
		val file= File(pushFiles[index!!])
		call.response.header("Content-Disposition", "attachment; filename=${file.name}")
		call.respondFile(file)
	}

	fun generatePushFilesHtml(): String  = pushFiles.withIndex().joinToString("\n") {
		val escaped = escapeHtml(File(it.value).name)
		"<li><a href=\"download?i=${it.index}\">$escaped</a></li>"
	}
}
