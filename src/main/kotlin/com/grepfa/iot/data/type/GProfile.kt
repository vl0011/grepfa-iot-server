package com.grepfa.iot.data.type

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

object GProfiles : UUIDTable() {

    // 프로필 이름
    val name = varchar("name", 50)
}

class GProfile(id: EntityID<UUID>): UUIDEntity(id) {
    companion object : UUIDEntityClass<GProfile>(GProfiles)

    var name by GProfiles.name
}