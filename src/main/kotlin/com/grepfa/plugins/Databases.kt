package com.grepfa.plugins

import com.grepfa.iot.api.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database
import java.util.*

fun Application.configureDatabases() {
    val database = Database.connect(
        url = "jdbc:postgresql://cs.grepfa.com:5432/grepfa",
        driver = "org.postgresql.Driver",
        user = "grepfa",
        password = "grepfa"
    )

    routing {
        route("/api") {
            route("/profile") {
                post("/add") {
                    try {
                        val id = ProfileAPI.newProfile(call.receive<GNewProfileRequest>()).toString()
                        call.respond(mapOf("id" to id))
                    } catch (e: Exception) {
                        call.respondText("error", status = HttpStatusCode.BadRequest)
                    }
                }
                get("/get_profile_request_demo") {
                    call.respond(
                        GNewProfileRequest(
                            listOf<GPartData>(
                                GPartData(
                                    id = "dont use this field",
                                    "name",
                                    "sensor",
                                    "integer",
                                    "summary",
                                    "desc",
                                    "mm",
                                    0.0,
                                    10.0
                                )
                            ), "name"
                        )
                    )
                }
                delete("/{profileId?}") {
                    val id = call.parameters["profileId"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
                    try {
                        ProfileAPI.delProfile(UUID.fromString(id))
                        call.respond(mapOf("result" to "ok"))
                    } catch (e: Exception) {
                        call.respondText("error", status = HttpStatusCode.BadRequest)
                    }
                }
            }
            route("/device") {
                post("/add") {
                    try {
                        val id = DeviceAPI.newDevice(call.receive()).first.toString()
                        call.respond(mapOf("id" to id))
                    } catch (e: Exception) {
                        call.respondText("error", status = HttpStatusCode.BadRequest)
                    }
                }
                get("/get_request_request_demo") {
                    call.respond(NewDeviceRequest(
                        deviceName = "name",
                        profileId = "uuid-str",
                        network = Network.LORAWAN,
                        address = "lora-eui64",
                        summary = "summary",
                        description = "desc"
                    ))
                }
                route("/info") {
                    get("/phy_address/{phyAddress?}") {
                        val phyAddr = call.parameters["phyAddress"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                        call.respond(DeviceAPI.findPartsFromPhyAddress(phyAddr))
                    }
                    get("/uuid/{phyAddress?}") {
                        val phyAddr = call.parameters["phyAddress"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                        call.respond(DeviceAPI.findPartsFromDeviceUUID(phyAddr))
                    }
                }
                delete("{deviceId?}"){
                    val id = call.parameters["deviceId"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
                    try {
                        DeviceAPI.delDevice(UUID.fromString(id))
                        call.respond(mapOf("result" to "ok"))
                    } catch (e: Exception) {
                        call.respondText("error", status = HttpStatusCode.BadRequest)
                    }
                }
            }
        }
    }
}


