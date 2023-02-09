package com.grepfa.iot.api

import com.grepfa.iot.data.type.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

enum class PartType(val columnName: String) {
    ACTUATOR("actuator"),
    SENSOR("sensor")
}

enum class PartVarType(val columnName: String) {
    FLOAT("float"),
    INTEGER("integer"),
    STRING("string")
}


@Serializable
data class GNewProfileRequest(
    val parts: List<GPartData>,
    val profileName: String,
)

@Serializable
data class GPartData(
    // 파츠 id
    val id: String = "",

    // 파츠 이름
    val name: String,

    // sensor or actuator ...
    val type: String,

    // 변수 타입 float int string ...
    val varType: String,

    // 파츠 설명 요약
    val summary:String = "",

    // 파츠 설명
    val desc:String = "",

    // 값의 단위
    val unit: String = "",

    // 값의 최소값
    val min:Double = 0.0,

    // 값의 최대값
    val max:Double = 0.0,
)

object ProfileAPI {

    fun newProfile(
        request: GNewProfileRequest
    ) : UUID {
        var ret : UUID? = null
        transaction {
            SchemaUtils.create(GParts, GProfiles)
            val t = GProfile.new {
                name = request.profileName
            }
            request.parts.forEach { part ->
                GPart.new {
                    profile = t
                    name = part.name
                    type = part.type
                    summary = part.summary
                    desc = part.desc
                    varType = part.varType
                    unit = part.unit
                    min = part.min
                    max = part.max
                }
            }
            ret = t.id.value
        }
        return ret!!
    }

    fun delProfile(id: UUID) {
        transaction {
            GPart.findById(id)?.delete()
        }
    }
}