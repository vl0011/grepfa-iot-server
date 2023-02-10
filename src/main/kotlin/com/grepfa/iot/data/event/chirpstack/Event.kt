

package com.grepfa.iot.data.event.chirpstack
import com.fasterxml.jackson.annotation.JsonProperty
import com.grepfa.iot.api.Network
import com.grepfa.iot.data.event.grepfa.*

interface IEvBase{
    fun ToGEv() : IGEvBase
}
data class EvUp(
    var `data`: String,
    var deduplicationId: String,
    var devAddr: String,
    var deviceInfo: DeviceInfo,
    var dr: Int,
    var fPort: Int,
    var rxInfo: List<RxInfo>,
    var time: String,
    var txInfo: TxInfo
): IEvBase{
    data class DeviceInfo(
        var applicationId: String,
        var applicationName: String,
        var devEui: String,
        var deviceName: String,
        var deviceProfileId: String,
        var deviceProfileName: String,
        var tags: Tags,
        var tenantId: String,
        var tenantName: String
    ) {
        data class Tags(
            var key: String
        )
    }

    data class RxInfo(
        var context: String,
        var gatewayId: String,
        var metadata: Metadata,
        var rssi: Int,
        var snr: Double,
        var uplinkId: Long
    ) {
        data class Metadata(
            @JsonProperty("region_common_name")
            var regionCommonName: String,
            @JsonProperty("region_name")
            var regionName: String
        )
    }

    data class TxInfo(
        var frequency: Int,
        var modulation: Modulation
    ) {
        data class Modulation(
            var lora: Lora
        ) {
            data class Lora(
                var bandwidth: Int,
                var codeRate: String,
                var spreadingFactor: Int
            )
        }
    }

    override fun ToGEv(): IGEvBase {
        return GEvUp(
            address = this.deviceInfo.devEui,
            network = Network.LORAWAN.columnName,
            msgId = this.deduplicationId,
            data = this.data,
            time = this.time
        )
    }

}

data class EvStatus(
    var batteryLevel: Double,
    var deduplicationId: String,
    var deviceInfo: DeviceInfo,
    var margin: Int,
    var time: String
) : IEvBase {
    data class DeviceInfo(
        var applicationId: String,
        var applicationName: String,
        var devEui: String,
        var deviceName: String,
        var deviceProfileId: String,
        var deviceProfileName: String,
        var tenantId: String,
        var tenantName: String
    )

    override fun ToGEv(): IGEvBase {
        return GEvStatus(
            address = this.deviceInfo.devEui,
            network = Network.LORAWAN.columnName,
            batLv = this.batteryLevel,
            margin = this.margin,
            time = this.time
        )
    }
}

data class EvJoin(
    var batteryLevel: Double,
    var deduplicationId: String,
    var deviceInfo: DeviceInfo,
    var margin: Int,
    var time: String
) : IEvBase {
    data class DeviceInfo(
        var applicationId: String,
        var applicationName: String,
        var devEui: String,
        var deviceName: String,
        var deviceProfileId: String,
        var deviceProfileName: String,
        var tenantId: String,
        var tenantName: String
    )

    override fun ToGEv(): IGEvBase {
        return GEvJoin(
            address = this.deviceInfo.devEui,
            network = Network.LORAWAN.columnName,
            msgId = this.deduplicationId,
            time = this.time
        )
    }
}

data class EvAck(
    var acknowledged: Boolean,
    var deduplicationId: String,
    var deviceInfo: DeviceInfo,
    var fCntDown: Int,
    var queueItemId: String,
    var time: String
) :IEvBase {
    data class DeviceInfo(
        var applicationId: String,
        var applicationName: String,
        var devEui: String,
        var deviceName: String,
        var deviceProfileId: String,
        var deviceProfileName: String,
        var tags: Tags,
        var tenantId: String,
        var tenantName: String
    ) {
        data class Tags(
            var key: String
        )
    }

    override fun ToGEv(): IGEvBase {
        return GEvAck(
            address = this.deviceInfo.devEui,
            network = Network.LORAWAN.columnName,
            msgId = this.deduplicationId,
            queueId = this.queueItemId,
            time = this.time
        )
    }
}

data class EvTxAck(
    var deviceInfo: DeviceInfo,
    var downlinkId: Long,
    var fCntDown: Int,
    var gatewayId: String,
    var queueItemId: String,
    var time: String,
    var txInfo: TxInfo
):IEvBase {
    data class DeviceInfo(
        var applicationId: String,
        var applicationName: String,
        var devEui: String,
        var deviceName: String,
        var deviceProfileId: String,
        var deviceProfileName: String,
        var tags: Tags,
        var tenantId: String,
        var tenantName: String
    ) {
        data class Tags(
            var key: String
        )
    }

    data class TxInfo(
        var context: String,
        var frequency: Int,
        var modulation: Modulation,
        var power: Int,
        var timing: Timing
    ) {
        data class Modulation(
            var lora: Lora
        ) {
            data class Lora(
                var bandwidth: Int,
                var codeRate: String,
                var polarizationInversion: Boolean,
                var spreadingFactor: Int
            )
        }

        data class Timing(
            var delay: Delay
        ) {
            data class Delay(
                var delay: String
            )
        }
    }

    override fun ToGEv(): IGEvBase {
        return GEvTxAck(
            address = this.deviceInfo.devEui,
            network = Network.LORAWAN.columnName,
            queueId = this.queueItemId,
            time = this.time,
            downlinkId = this.downlinkId
        )
    }
}

data class EVLog(
    var code: String,
    var context: Context,
    var description: String,
    var deviceInfo: DeviceInfo,
    var level: String,
    var time: String
) :IEvBase {
    data class Context(
        @JsonProperty("deduplication_id")
        var deduplicationId: String
    )

    data class DeviceInfo(
        var applicationId: String,
        var applicationName: String,
        var devEui: String,
        var deviceName: String,
        var deviceProfileId: String,
        var deviceProfileName: String,
        var tags: Tags,
        var tenantId: String,
        var tenantName: String
    ) {
        data class Tags(
            var key: String
        )
    }

    override fun ToGEv(): IGEvBase {
        return GEvLog(
            address = this.deviceInfo.devEui,
            network = Network.LORAWAN.columnName,
            code = this.code,
            description = this.description,
            level = this.level,
            time = this.time
        )
    }
}

data class EvLocation(
    var deduplicationId: String,
    var deviceInfo: DeviceInfo,
    var location: Location,
    var time: String
) :IEvBase{
    data class DeviceInfo(
        var applicationId: String,
        var applicationName: String,
        var devEui: String,
        var deviceName: String,
        var deviceProfileId: String,
        var deviceProfileName: String,
        var tags: Tags,
        var tenantId: String,
        var tenantName: String
    ) {
        data class Tags(
            var key: String
        )
    }

    data class Location(
        var altitude: Double,
        var latitude: Double,
        var longitude: Double
    )

    override fun ToGEv(): IGEvBase {
        return GEvLocation(
            address = this.deviceInfo.devEui,
            network = Network.LORAWAN.columnName,
            altitude = this.location.altitude,
            longitude =  this.location.longitude,
            latitude = this.location.latitude,
            msgId = this.deduplicationId,
            time = this.time
        )
    }
}