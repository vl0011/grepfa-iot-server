package com.grepfa.plugins

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.grepfa.iot.api.GNewProfileRequest
import com.grepfa.iot.api.ProfileAPI
import com.grepfa.iot.data.event.grepfa.GEvMsg
import com.grepfa.iot.data.eventListener.EventListener
import com.grepfa.iot.data.eventListener.ForwardingURLs
import com.grepfa.iot.nego.ChirpStackConnectionOptions
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
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


    val el = EventListener(opt, this.coroutineContext)

    routing {
        route("/forwarder") {
            route("/config") {
                get {
                    call.respond(ForwardingURLs())
                }
                post {
                }
            }
        }
    }
}

