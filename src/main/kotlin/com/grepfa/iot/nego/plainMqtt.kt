package com.grepfa.iot.nego

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.grepfa.iot.api.DeviceAPI
import com.grepfa.iot.data.event.chirpstack.IEvBase

import com.grepfa.iot.data.event.grepfa.GEvMsg
import com.grepfa.iot.data.event.plain.PEvUp
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.slf4j.LoggerFactory
import java.util.*


// PlainMqtt 의 mqtt와 통신하는거
class PlainMqttNegotiator(val opt: ChirpStackConnectionOptions) {
    val logger = LoggerFactory.getLogger(javaClass)
    val persistence = MemoryPersistence()
    val mqttClient = MqttAsyncClient(opt.broker, opt.clientId, persistence)

    val up = SubCbUp()
//    val join = SubCbJoin()
//    val ack = SubCbAck()
//    val txAck = SubCbTxAck()
//    val log = SubCbLog()
//    val location = SubCbLocation()
//    val status = SubCbStatus()

    val cbList = arrayOf(
        up,
//        join,
//        ack,
//        txAck,
//        log,
//        location,
//        status,
    )
    val topicList = Array<String>(cbList.size) { i -> cbList[i].topic }
    val qosList = IntArray(cbList.size) { opt.qos }

    init {
        try {
            logger.info("start connecting PlainMqtt mqtt server...")
            val connOpts = MqttConnectOptions()
            connOpts.isCleanSession = true
            connOpts.password = "grepfa".toCharArray()
            connOpts.userName = "grepfa"

            logger.info("Connecting to broker: $opt.broker")

            mqttClient.connect(connOpts).waitForCompletion()
            logger.info("connect success!")
            topicList.forEach {
                logger.info("try subscript topic :\"${it}\"")
            }
            mqttClient.subscribe(topicList, qosList, cbList)
            logger.info("subscribe topics success!")

        } catch (me: MqttException) {
            logger.error("connect mqtt server error ${me.message}\n${me.stackTraceToString()}")
//            exitProcess(1)
        }

    }


    fun topicDevEuiParser(topic: String?): String {
        if (topic == null) {
            throw NullPointerException("topic is null")
        }
        val l = topic.split('/')
        if (l.isEmpty() || l.size != 6) {
            throw InvalidEventTopicException("topic $topic is invalid")
        }
        return l[3]
    }

    abstract inner class SubCb<T : IEvBase> : IMqttMessageListener {
        abstract fun type(): TypeReference<T>
        abstract val eventType: String
        abstract val topic: String
        open var callback: (GEvMsg) -> Unit = {}
        var eui = ""
        override fun messageArrived(topic: String?, message: MqttMessage?) {
            try {
                eui = topicDevEuiParser(topic)
                logger.info("device \"$eui\" send message:\n${message.toString()}")
                val mapper = jacksonObjectMapper()
                val payload = mapper.readValue(message.toString(), type())
                val gev = payload.ToGEv()
                callback(DeviceAPI.getEventAdditionalData(gev))
            } catch (e: InvalidEventTopicException) {
                logger.warn("topic string \"topic\" ${e.message}")
            } catch (e: InvalidDeviceException) {
                logger.warn("mismatch eui topic and payload data: ${e.message}")
            } catch (e: Exception) {
                logger.warn("unknown exception: ${e.message} ${e.stackTraceToString()}")
            }
        }
    }

    inner class SubCbUp : SubCb<PEvUp>() {
        private val typeReference = object : TypeReference<PEvUp>() {}
        override fun type(): TypeReference<PEvUp> {
            return typeReference
        }

        override val eventType = "up"
        override val topic = "grepfa/${opt.appId}/device/+/event/$eventType"
    }
//
//    inner class SubCbJoin : SubCb<EvJoin>() {
//        private val typeReference = object : TypeReference<EvJoin>() {}
//        override fun type(): TypeReference<EvJoin> {
//            return typeReference
//        }
//
//        override val eventType = "join"
//        override val topic = "application/${opt.appId}/device/+/event/$eventType"
//    }
//
//    inner class SubCbAck : SubCb<EvAck>() {
//        private val typeReference = object : TypeReference<EvAck>() {}
//        override fun type(): TypeReference<EvAck> {
//            return typeReference
//        }
//
//        override val eventType = "ack"
//        override val topic = "application/${opt.appId}/device/+/event/$eventType"
//    }
//
//    inner class SubCbTxAck : SubCb<EvTxAck>() {
//        private val typeReference = object : TypeReference<EvTxAck>() {}
//        override fun type(): TypeReference<EvTxAck> {
//            return typeReference
//        }
//
//        override val eventType = "txack"
//        override val topic = "application/${opt.appId}/device/+/event/$eventType"
//    }
//
//    inner class SubCbLog : SubCb<EVLog>() {
//        private val typeReference = object : TypeReference<EVLog>() {}
//        override fun type(): TypeReference<EVLog> {
//            return typeReference
//        }
//
//        override val eventType = "log"
//        override val topic = "application/${opt.appId}/device/+/event/$eventType"
//    }
//
//    inner class SubCbLocation : SubCb<EvLocation>() {
//        private val typeReference = object : TypeReference<EvLocation>() {}
//        override fun type(): TypeReference<EvLocation> {
//            return typeReference
//        }
//
//        override val eventType = "location"
//        override val topic = "application/${opt.appId}/device/+/event/$eventType"
//    }
//
//    inner class SubCbStatus : SubCb<EvStatus>() {
//        private val typeReference = object : TypeReference<EvStatus>() {}
//        override fun type(): TypeReference<EvStatus> {
//            return typeReference
//        }
//
//        override val eventType = "status"
//        override val topic = "application/${opt.appId}/device/+/event/$eventType"
//    }

    fun down(eui: String, data: String, fPort: Int, confirmed: Boolean = false) {
        val encoder = Base64.getEncoder()

        val payload = DownLinkPayload(
            confirmed = confirmed,
            fPort = fPort,
            data = encoder.encode(data.toByteArray()),
            devEui = eui
        )

        val payloadJson = ObjectMapper().writeValueAsBytes(payload)

        try {
            mqttClient.publish("application/${opt.appId}/device/$eui/command/down", payloadJson, opt.qos, false)
            logger.info("downlink published")
        } catch (e: MqttPersistenceException) {
            logger.warn("dpwn link scheduling fail ${e.message}\n${e.stackTraceToString()}")
        }
    }

    fun down(eui: String, data: Any, fPort: Int, confirmed: Boolean = false) {
        down(eui, ObjectMapper().writeValueAsString(data), fPort, confirmed)
    }
}

data class DownLinkPayload(
    var confirmed: Boolean,
    var `data`: ByteArray,
    var devEui: String,
    var fPort: Int
)


class InvalidEventTopicException(message: String?) : RuntimeException(message) {}
class InvalidDeviceException(message: String?) : RuntimeException(message) {}