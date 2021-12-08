package kr.or.mrhi.mycoinkt

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class LoadingActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_activity)
        startLoading()
    }

    private fun startLoading() {
        val handler = Handler()
        handler.postDelayed({ finish() }, 2000)
    }
}
