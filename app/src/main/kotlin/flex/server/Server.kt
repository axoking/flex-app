package flex.server

import io.ktor.server.engine.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.netty.*
import flex.server.handleApi

class Server {
    val ktorServer = embeddedServer(Netty, port = 56789) {
        routing {
            get("/api") {
                handleApi(call)
            }
        }
    }
    
    fun start() {
        println("Server running")
        ktorServer.start(wait = true)
    }
}