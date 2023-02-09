package com.grepfa.iot.data.event.grepfa
import com.fasterxml.jackson.annotation.JsonProperty


data class GEvUp(
    var address: String,
    var msgId: String,
    var `data`: String,
    var deviceUniqueId: String,
    var network: String,
    var time :String
)

data class GEvStatus(
    var batLv: String,
    var msgId: String,
    var deviceUniqueId: String,
    var margin: String,
    var time: String
)

data class GEvJoin(
    var address: String,
    var msgId: String,
    var deviceUniqueId: String,
    var network: String,
    var time: String
)

data class GEvAck(
    var deviceUniqueId: String,
    var address: String,
    var msgId: String,
    var network: String,
    var queueId: String,
    var time: String
)

data class GEvTxAck(
    var deviceUniqueId: String,
    var address: String,
    var network: String,
    var queueId: String,
    var time: String
)

data class GEvLog(
    var address: String,
    var code: String,
    var description: String,
    var deviceUniqueId: String,
    var level: String,
    var network: String,
    var time: String
)

data class GEvLocation(
    var altitude: Double,
    var latitude: Double,
    var longtitude: Double,
    var msgId: String,
    var time: String
)