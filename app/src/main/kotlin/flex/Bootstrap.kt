package flex;

import flex.server.Server
import flex.client.Client
import kotlinx.coroutines.*

fun main() = runBlocking {
    launch {
        Server().start()
    }

    val con = Client()
    con.connect("localhost")
}