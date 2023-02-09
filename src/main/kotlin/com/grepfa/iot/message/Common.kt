package com.grepfa.iot.message

import java.time.LocalDate
import java.util.UUID

open class CommonData {
    var msgUUID: UUID? = null
    var deviceName: String = ""
    var msgTime: LocalDate? = null

}


// 업링크 메세지 페이로드 클래스
class CommonUp : CommonData() {
    var data: String = ""
}

// 다운링크 메세지 페이로드 클래스
class CommonDown: CommonData() {

}

// 상태 메세지 페이로드 클래스
class CommonStatus: CommonData() {

}

// 네트워크 가입 요청 페이로드 클래스
class CommonJoin: CommonData() {

}

