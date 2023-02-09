package com.grepfa.iot.data.type

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.UUID




object GParts : UUIDTable() {

    // 파츠 이름
    val name = varchar("name", 50)

    // sensor or actuator ...
    val type = varchar("type", 10)

    // 파츠 설명 요약
    val summary = text("summary").default("")

    // 파츠 설명
    val desc = text("description").default("")

    // 변수 타입 float int string ...
    val varType = varchar("v_type", 10)

    // 값의 단위
    val unit = varchar("unit", 10).default("")

    // 값의 최소값
    val min = double("min").default(.0)

    // 값의 최대값
    val max = double("max").default(.0)

    // 적용된 프로파일
    val profile = reference("profile", GProfiles)
}

class GPart(id: EntityID<UUID>): UUIDEntity(id) {
    companion object : UUIDEntityClass<GPart>(GParts)

    var name by GParts.name
    var type by GParts.type
    var summary by GParts.summary
    var desc by GParts.desc
    var varType by GParts.varType
    var unit by GParts.unit
    var min by GParts.min
    var max by GParts.max
    var profile by GProfile referencedOn GParts.profile
}