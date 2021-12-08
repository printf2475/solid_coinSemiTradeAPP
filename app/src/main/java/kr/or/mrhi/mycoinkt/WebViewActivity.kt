package kr.or.mrhi.mycoinkt

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import android.graphics.Typeface
import android.os.Bundle
import kr.or.mrhi.mycoinkt.graph.DemoBase
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import android.widget.TextView
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.content.Intent
import android.widget.BaseAdapter
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kr.or.mrhi.mycoinkt.adapter.OwnBankAdapter
import kr.or.mrhi.mycoinkt.adapter.CoinListAdapter
import kr.or.mrhi.mycoinkt.adapter.WalletTabLayoutAdapter
import android.widget.EditText
import com.google.android.material.tabs.TabLayout
import kr.or.mrhi.mycoinkt.DBController
import kr.or.mrhi.mycoinkt.R
import androidx.lifecycle.ViewModelProvider
import kr.or.mrhi.mycoinkt.CoinMain
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import android.text.TextWatcher
import android.text.Editable
import kr.or.mrhi.mycoinkt.MainActivity
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.app.Activity
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebChromeClient

class WebViewActivity : AppCompatActivity() {
    private lateinit var wv: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)
        val intent = intent
        val link = intent.getStringExtra("Link")

        //얻어온 링크주소를 웹뷰에 보여주기.
        wv = findViewById(R.id.wv)

        //웹페이지에서 사용하는 Javascript를 동작하도록
        wv.getSettings().javaScriptEnabled = true

        //웹뷰에 페이지를 load하면 안드로이드에서
        //자동으로 새로운 웹브라우저를 열어버림
        //그걸 안하고 내 WebView에 페이지를 보이도록
        wv.setWebViewClient(WebViewClient())

        //웹 페이지안에 웹다이얼로그를 보여주는 등의
        //작업 있다면.. 그걸 동작하도록.
        wv.setWebChromeClient(WebChromeClient())
        wv.loadUrl(link!!)
    }
}