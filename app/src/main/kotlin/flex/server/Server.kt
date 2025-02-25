package flex.server

import flex.Log
import io.ktor.http.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.isRegularFile

const val MAX_PUSH_FILES = 10
const val MAX_PULL_FILES = 16
const val MAX_PULL_BYTES = 32_000_000_000u // 32 GB

class Server(private val doLogging: Boolean) {
	var isPushing = false
	var isPulling = false
	var pushFiles: MutableList<String> = mutableListOf()
		private set
	var pullQueue: MutableList<PullRequest> = mutableListOf()
		private set

	private val ktorServer = embeddedServer(Netty, port = 56789) {
		routing {
			staticFiles("/static", File("src/main/resources/web/static"))

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
		if (doLogging) Log.info("HTTP server running")
		ktorServer.start(wait = wait)
	}

	fun stop() {
		if (doLogging) Log.info("Terminating server")
		ktorServer.stop()
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

	private fun addPullQueueEntry(rq: PullRequest) {
		pullQueue.add(rq)
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
		val size = args["size"]?.toULong()
		val name = args["name"]
		if (size == null || name == null || size > MAX_PULL_BYTES) {
			call.response.status(HttpStatusCode.BadRequest)
			return
		}
		addPullQueueEntry(PullRequest(name, size, call.request.origin.remoteAddress))
	}

	private fun generatePushFilesHtml(): String  = pushFiles.withIndex().joinToString("\n") {
		val escaped = escapeHtml(File(it.value).name)
		"<li><a href=\"download?i=${it.index}\">$escaped</a></li>"
	}
}
