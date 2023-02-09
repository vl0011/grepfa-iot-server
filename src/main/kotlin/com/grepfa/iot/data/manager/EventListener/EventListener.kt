package com.grepfa.iot.data.manager.EventListener

import com.grepfa.iot.nego.ChirpStackConnectionOptions
import com.grepfa.iot.nego.ChirpStackNegotiator



class EventListener(csOpt: ChirpStackConnectionOptions) {
    val cs = ChirpStackNegotiator(csOpt)

    init {
        cs.up.callback = {x ->


        }
        cs.join.callback = {x -> }
        cs.status.callback = {x -> }
        cs.log.callback = {x -> }
        cs.ack.callback = {x -> }
        cs.txAck.callback = {x -> }
        cs.location.callback = {x -> }
    }
}