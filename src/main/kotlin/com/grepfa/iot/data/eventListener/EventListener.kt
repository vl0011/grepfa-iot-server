package com.grepfa.iot.data.eventListener

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.grepfa.iot.data.event.grepfa.GEvMsg
import com.grepfa.iot.nego.ChirpStackConnectionOptions
import com.grepfa.iot.nego.ChirpStackNegotiator
import com.grepfa.iot.nego.PlainMqttNegotiator
import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import org.slf4j.LoggerFactory
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.jackson.*
import kotlin.coroutines.CoroutineContext

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

class EventListener(csOpt: ChirpStackConnectionOptions, con: CoroutineContext) {
    val logger = LoggerFactory.getLogger(javaClass)
    val cs = ChirpStackNegotiator(csOpt)
    val pm = PlainMqttNegotiator(csOpt)
    val eq = EventQueue()

    fun commonProcess(m: GEvMsg) {
        logger.info(jacksonObjectMapper().writeValueAsString(m))
    }

    init {
        eq.setCallbackURL(
            "http://localhost:8080/forwarder/test/up",
            "",
            "",
            "",
            "",
            "",
            ""

        )

        cs.up.callback = { this.commonProcess(it);eq.addUp(it) }
        cs.join.callback = { this.commonProcess(it);eq.addJoin(it) }
        cs.status.callback = { this.commonProcess(it);eq.addStatus(it) }
        cs.log.callback = { this.commonProcess(it);eq.addLog(it) }
        cs.ack.callback = { this.commonProcess(it);eq.addAck(it) }
        cs.txAck.callback = { this.commonProcess(it);eq.addTxAck(it) }
        cs.location.callback = { this.commonProcess(it);eq.addLocation(it) }

        pm.up.callback = { this.commonProcess(it);eq.addUp(it) }
    }

}

class EventQueue {
    val logger = LoggerFactory.getLogger(javaClass)
    var cb: CallbackURLs? = null
    fun setCallbackURL(
        up: String,
        join: String,
        status: String,
        ack: String,
        txAck: String,
        log: String,
        location: String
    ) {
        cb = CallbackURLs(
            up,
            join,
            status,
            ack,
            txAck,
            log,
            location
        )
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun addUp(payload: GEvMsg) {
        if (cb == null) {
            logger.warn("not set urls")
            return
        }
        val client = HttpClient(Java)
        val ret = jacksonObjectMapper().writeValueAsString(payload)
        GlobalScope.launch {
            client.post(cb!!.up) {
                contentType(ContentType.Application.Json)
                setBody(ret)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun addJoin(payload: GEvMsg) {
        if (cb == null) {
            logger.warn("not set urls")
            return
        }
        val client = HttpClient(Java)
        GlobalScope.launch {
            client.post(cb!!.join) {
                contentType(ContentType.Application.Json)
                setBody(jacksonObjectMapper().writeValueAsString(payload))
            }
        }

    }

    @OptIn(DelicateCoroutinesApi::class)
    fun addStatus(payload: GEvMsg) {
        if (cb == null) {
            logger.warn("not set urls")
            return
        }
        val client = HttpClient(Java) {
        }
        GlobalScope.launch {
            client.post(cb!!.status) {
                contentType(ContentType.Application.Json)
                setBody(jacksonObjectMapper().writeValueAsString(payload))
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun addAck(payload: GEvMsg) {
        if (cb == null) {
            logger.warn("not set urls")
            return
        }
        val client = HttpClient(Java) {
        }
        GlobalScope.launch {
            client.post(cb!!.ack) {
                contentType(ContentType.Application.Json)
                setBody(jacksonObjectMapper().writeValueAsString(payload))
            }
        }

    }

    @OptIn(DelicateCoroutinesApi::class)
    fun addTxAck(payload: GEvMsg) {
        if (cb == null) {
            logger.warn("not set urls")
            return
        }
        val client = HttpClient(Java) {
        }
        GlobalScope.launch {
            client.post(cb!!.txAck) {
                contentType(ContentType.Application.Json)
                setBody(jacksonObjectMapper().writeValueAsString(payload))
            }
        }

    }

    @OptIn(DelicateCoroutinesApi::class)
    fun addLog(payload: GEvMsg) {
        if (cb == null) {
            logger.warn("not set urls")
            return
        }
        val client = HttpClient(Java) {
        }
        GlobalScope.launch {
            client.post(cb!!.log) {
                contentType(ContentType.Application.Json)
                setBody(jacksonObjectMapper().writeValueAsString(payload))
            }
        }

    }

    @OptIn(DelicateCoroutinesApi::class)
    fun addLocation(payload: GEvMsg) {
        if (cb == null) {
            logger.warn("not set urls")
            return
        }
        val client = HttpClient(Java) {
        }
        GlobalScope.launch {
            client.post(cb!!.location) {
                contentType(ContentType.Application.Json)
                setBody(jacksonObjectMapper().writeValueAsString(payload))
            }
        }

    }
}