package com.grepfa.iot.api

import com.grepfa.iot.data.event.grepfa.GEvMsg
import com.grepfa.iot.data.event.grepfa.IGEvBase
import com.grepfa.iot.data.type.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import java.security.MessageDigest
import java.util.*
import kotlin.collections.ArrayList

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
    val logger = LoggerFactory.getLogger(javaClass)
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

    fun findPartsFromPhyAddress(phyAddress: String): GDeviceData {
        logger.info("called: find parts from physical address")
        return transaction {
            val query = GDevices.innerJoin(GProfiles).innerJoin(GParts)
                .select {
                    GDevices.address eq phyAddress and (GParts.type eq PartType.SENSOR.columnName)
                }
            val di = GDevice.wrapRows(query).first()

            val partsList = ArrayList<GPartData>()
            GPart.wrapRows(query).forEach() { x ->
                partsList.add(
                    GPartData(
                        id = x.id.value.toString(),
                        name = x.name,
                        type = x.type,
                        varType = x.varType,
                        summary = x.summary,
                        desc = x.desc,
                        unit = x.unit,
                        min = x.min,
                        max = x.max,
                    )
                )
            }
            GDeviceData(
                parts = partsList,
                name = di.name,
                summary = di.summary,
                description = di.desc,
                network = di.network,
                address = di.address,
                profileId = di.profile.id.value.toString(),
                deviceId = di.id.value.toString()
            )
        }
    }

    fun findPartsFromDeviceUUID(uuid: String): GDeviceData {
        logger.info("called: find parts from device uuid")
        return transaction {
            val query = GDevices.innerJoin(GProfiles).innerJoin(GParts)
                .select {
                    GDevices.id eq UUID.fromString(uuid) and (GParts.type eq PartType.SENSOR.columnName)
                }
            val di = GDevice.wrapRows(query).first()

            val partsList = ArrayList<GPartData>()
            GPart.wrapRows(query).forEach() { x ->
                partsList.add(
                    GPartData(
                        id = x.id.value.toString(),
                        name = x.name,
                        type = x.type,
                        varType = x.varType,
                        summary = x.summary,
                        desc = x.desc,
                        unit = x.unit,
                        min = x.min,
                        max = x.max,
                    )
                )
            }
            GDeviceData(
                parts = partsList,
                name = di.name,
                summary = di.summary,
                description = di.desc,
                network = di.network,
                address = di.address,
                profileId = di.profile.id.value.toString(),
                deviceId = di.id.value.toString()
            )
        }
    }

    fun getEventAdditionalData(gev: IGEvBase) : GEvMsg {
        if (gev.network == Network.LORAWAN.columnName) {
            val gdd = findPartsFromPhyAddress(gev.address)
            return GEvMsg(
                deviceId = gdd.deviceId,
                profileId = gdd.profileId,
                ev = gev
            )
        }
        TODO("wifi")
    }

    fun payloadParser(msg: String) {
        TODO()
    }
}


    @Serializable
    data class GDeviceData(
        val name: String,
        val summary: String,
        val description: String,
        val network: String,
        val address: String,
        val profileId: String,
        val deviceId: String,
        val parts: List<GPartData>
    )

