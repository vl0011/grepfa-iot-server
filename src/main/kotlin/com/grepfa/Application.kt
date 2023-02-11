package com.grepfa

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.grepfa.plugins.*

fun main() {

    embeddedServer(Netty, port = 47000, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureDatabases()
    configureRouting()
    configureForwarder()
}
