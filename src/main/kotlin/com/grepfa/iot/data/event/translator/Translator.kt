package com.grepfa.iot.data.event.translator

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.grepfa.iot.api.PartType
import com.grepfa.iot.data.type.GDevice
import com.grepfa.iot.data.type.GDevices
import com.grepfa.iot.data.type.GParts
import com.grepfa.iot.data.type.GProfiles
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

object Translator {
    fun loraEuiToGrepfaDeviceUUID(phyAddress: String) : UUID {
        var ret: UUID? = null
        transaction {
            val devices = GDevice.find {
                GDevices.address eq phyAddress
            }

            if (devices.empty()) {

            }

            devices.forEach { x ->
                ret = x.id.value
                return@forEach
            }
        }
        return ret!!
    }

    fun payloadParser(dataStr: String, phyAddress: String) {
        transaction {

        }
        val dec = Base64.getDecoder()
        val decoded = dec.decode(dataStr)
        val mapper = jacksonObjectMapper()
        val tr = object : TypeReference<List<PayloadValue>>(){}
        val data = mapper.readValue(decoded, tr)

        val partList = transaction {
            val query = GDevices.innerJoin(GProfiles).innerJoin(GParts)
                .select{
                    GDevices.address eq phyAddress and (GParts.type eq PartType.SENSOR.columnName)
                }
            query.toList()
        }

        data.forEach() {x ->

        }
    }
}

data class PayloadValue(
    val id: String,
    val value: String
)

data class PartsValue(
    val id :String,
    val name: String,
    val type: String,
    val summary:String,
    val desc:String,
    val varType:String,
    val unit:String,
    val min: Double,
    val max:Double,
)