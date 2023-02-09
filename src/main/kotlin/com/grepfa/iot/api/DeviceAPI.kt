package com.grepfa.iot.api

import com.grepfa.iot.data.type.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.security.MessageDigest
import java.util.*

enum class Network(val columnName: String) {
    LORAWAN("lorawan"),
    TCP_IP("tcp_ip")
}

@Serializable
data class NewDeviceRequest(
    val deviceName: String,
    val profileId: String,
    val network: Network,
    val address: String,
    val summary: String = "",
    val description: String = "",
)

object DeviceAPI {
    fun newDevice(
        request: NewDeviceRequest
    ): Pair<UUID?, String> {
        var ret: UUID? = null
        val bytes = this.toString().toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val b = ByteArray(20)
        Random().nextBytes(b)
        md.update(b)
        val digest = md.digest(bytes)
        val keyStr = digest.fold("") { str, it -> str + "%02x".format(it) }


        transaction {
            SchemaUtils.create(GDevices, GProfiles)
            val p =
                GProfile.findById(UUID.fromString(request.profileId))
                    ?: throw NullPointerException("can't find profile ${request.profileId}")

            ret = GDevice.new {
                name = request.deviceName
                summary = request.summary
                desc = request.description
                key = keyStr
                profile = p
                address = request.address
                network = request.network.columnName
            }.id.value
        }

        return Pair(ret, keyStr)
    }

    fun delDevice(id: UUID) {
        transaction {
            GDevice.findById(id)?.delete()
        }
    }
}



