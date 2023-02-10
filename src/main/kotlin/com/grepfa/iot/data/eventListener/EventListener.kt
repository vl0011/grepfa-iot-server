package com.grepfa.iot.data.eventListener

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.grepfa.iot.data.event.grepfa.GEvMsg
import com.grepfa.iot.nego.ChirpStackConnectionOptions
import com.grepfa.iot.nego.ChirpStackNegotiator
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import org.slf4j.LoggerFactory
import java.net.http.HttpClient
@Serializable
data class ForwardingURLs(
    val upCallbackURL: String = "",
    val statusCallbackURL: String = "",
    val joinCallbackURL: String = "",
    val ackCallbackURL: String = "",
    val txAckCallbackURL: String = "",
    val logCallbackURL: String = "",
    val locationCallbackURL: String = ""
)

@Serializable
data class CallbackURLs(
    val up: String,
    val status: String,
    val join: String,
    val ack: String,
    val txAck: String,
    val log: String,
    val location: String,
)

class EventListener(csOpt: ChirpStackConnectionOptions) {
    val logger = LoggerFactory.getLogger(javaClass)
    val cs = ChirpStackNegotiator(csOpt)

    var cb: CallbackURLs? = null

    fun setURLs(c: CallbackURLs) {

    }

    fun commonProcess(m: GEvMsg) {
        logger.info(jacksonObjectMapper().writeValueAsString(m))
    }
    fun setCallbackURL(
        up: String,
        join: String,
        status: String,
        ack: String,
        txAck: String,
        log: String,
        location: String
    ) {

    }
    init {

        cs.up.callback = {
            this.commonProcess(it)
            if (cb != null) {
                val client = HttpClient(CIO)

            }
        }
        cs.join.callback = {
            this.commonProcess(it)

        }
        cs.status.callback = { this.commonProcess(it) }
        cs.log.callback = { this.commonProcess(it) }
        cs.ack.callback = { this.commonProcess(it) }
        cs.txAck.callback = { this.commonProcess(it) }
        cs.location.callback = { this.commonProcess(it) }
    }


    class EventQueue {
        suspend fun z() {

        }
        init {
            launch {

            }
        }
    }
}