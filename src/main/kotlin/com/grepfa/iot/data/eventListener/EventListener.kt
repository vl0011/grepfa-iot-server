package com.grepfa.iot.data.eventListener

import com.grepfa.iot.nego.ChirpStackConnectionOptions
import com.grepfa.iot.nego.ChirpStackNegotiator
import kotlinx.serialization.Serializable

@Serializable
data class ForwardingURLs(
    val upCallbackURL: String = "",
    val statusCallbackURL: String = "",
    val joinCallbackURL: String = "",
    val ackCallbackURL: String = "",
    val txAckCallbackURL: String = "",
    val logCallbackURL: String = "",
    val locationCallbackURL: String = ""
)


class EventListener(csOpt: ChirpStackConnectionOptions) {
    val cs = ChirpStackNegotiator(csOpt)


    init {
        cs.up.callback = {x -> }
        cs.join.callback = {x -> }
        cs.status.callback = {x -> }
        cs.log.callback = {x -> }
        cs.ack.callback = {x -> }
        cs.txAck.callback = {x -> }
        cs.location.callback = {x -> }
    }


}