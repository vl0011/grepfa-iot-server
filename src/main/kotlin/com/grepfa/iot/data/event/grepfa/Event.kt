package com.grepfa.iot.data.event.grepfa

import kotlinx.serialization.Serializable
import com.grepfa.iot.data.event.grepfa.IGEvBase as IGEvBase

@Serializable
data class GEvMsg(
    val deviceId: String,
    val profileId: String,
//    val forwardedTime:
    val ev: IGEvBase,
)

interface IGEvBase {
    val address: String
    val network: String
    val type: String
}

@Serializable
data class GEvUp(
    override val address: String,
    override val network: String,
    val msgId: String,
    val `data`: String,
    val time: String,
    override val type:String = "UP"
) : IGEvBase

@Serializable
data class GEvStatus(
    override val address: String,
    override val network: String,
    val batLv: Double,
    val margin: Int,
    val time: String,
    override val type:String = "STATUS"
) : IGEvBase

@Serializable
data class GEvJoin(
    override val address: String,
    override val network: String,
    val msgId: String,
    val time: String,
    override val type:String = "JOIN"
) : IGEvBase

@Serializable
data class GEvAck(
    override val address: String,
    override val network: String,
    val msgId: String,
    val queueId: String,
    val time: String,
    override val type:String = "ACK"
) : IGEvBase

@Serializable
data class GEvTxAck(
    override val address: String,
    override val network: String,
    val queueId: String,
    val downlinkId: Long,
    val time: String,
    override val type:String = "TXACK"
) : IGEvBase

@Serializable
data class GEvLog(
    override val address: String,
    override val network: String,
    val code: String,
    val description: String,
    val level: String,
    val time: String,
    override val type:String = "LOG"
) : IGEvBase

@Serializable
data class GEvLocation(
    override val address: String,
    override val network: String,
    val altitude: Double,
    val latitude: Double,
    val longitude: Double,
    val msgId: String,
    val time: String,
    override val type:String = "LOCATION"
) : IGEvBase