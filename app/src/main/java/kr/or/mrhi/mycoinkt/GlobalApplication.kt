package kr.or.mrhi.mycoinkt

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 다른 초기화 코드들

        // Kakao SDK 초기화
        KakaoSdk.init(this, "eb21b459f9ff918c201ddd09ce3b8bab")
    }
}