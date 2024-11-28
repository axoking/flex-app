package flex.server

import io.ktor.server.routing.*
import io.ktor.server.response.*

suspend fun handleApi(call: RoutingCall) {
	call.respondText("Hello, this is a flex server!")
}
