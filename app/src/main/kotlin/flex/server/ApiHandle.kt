/*

CURRENTLY UNUSED
meant for APIs to let flex-apps directly work with each other
instead of using the web interface

 */


package flex.server

import io.ktor.server.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import flex.api.*
import flex.values.*

suspend fun RoutingCall.respondArgs(vararg args: Pair<String, String>) {
	val map = args.toMap().toMutableMap()
	map["status"] = "ok"
	this.respondText(encodeArgs(map))
}

suspend fun RoutingCall.respondError(cause: String) {
	this.respondArgs("status" to "error", "cause" to cause)
}

class ApiHandle {
	companion object {
		suspend fun greet(call: RoutingCall, args: ApiArgs) {
			if (args["version"] != VERSION) {
				call.respondError("Invalid version")
			}
			else {
				call.respondArgs("version" to VERSION)
			}
		}

		suspend fun unknownAction(call: RoutingCall, args: ApiArgs) {

		}
	}
}
