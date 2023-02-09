package com.grepfa.iot.data.type

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

object GDevices: UUIDTable() {

    // 기기 이름
    val name = varchar("name", 50)

    // 기기 설명 요약
    val summary = text("summary")

    // 기기 설명
    val desc = text("description")

    // 기기 고유 키
    val key = char("key", 64)

    // 네트워크
    val network = varchar("network", 10)

    // 물리 주소
    val address = varchar("address", 100)

    // 프로파일
    val profile = reference("profile", GProfiles)
}

class GDevice(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object: UUIDEntityClass<GDevice>(GDevices)

    var name by GDevices.name
    var summary by GDevices.summary
    var desc by GDevices.desc
    var key by GDevices.key
    var network by GDevices.network
    var address by GDevices.address
    var profile by GProfile referencedOn GDevices.profile
}