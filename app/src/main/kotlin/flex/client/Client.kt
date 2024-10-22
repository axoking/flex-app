package flex.client

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.call.*

class Client {
    val con = HttpClient(CIO)
    val url: String

    constructor(host: String) {
        url = "http://$host:56789/api"
    }

    suspend fun greet() {
        println("getting $url")
        val resp = con.get(url)
        println(resp.body() as String)
    }
}