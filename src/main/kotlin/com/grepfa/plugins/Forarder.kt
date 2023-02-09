package com.grepfa.plugins

import com.grepfa.iot.data.eventListener.EventListener
import com.grepfa.iot.data.eventListener.ForwardingURLs
import com.grepfa.iot.nego.ChirpStackConnectionOptions
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.util.*

fun Application.configureForwarder() {
    val opt = ChirpStackConnectionOptions(
        "ssl://cs.grepfa.com:8883",
        "grepfa" + UUID.randomUUID().toString(),
        "appid",
        1
    )
//    try {
//        val el = EventListener(opt)
//    }   catch (e: Exception) {
//
//    }

    val el = EventListener(opt)

    routing {
        route("/forwarder") {
            route("/config") {
                get {
                    call.respond(ForwardingURLs())
                }
                post {

                }
            }
            route("demo") {

            }
        }
    }
}

