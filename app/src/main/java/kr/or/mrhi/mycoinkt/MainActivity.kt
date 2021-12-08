package kr.or.mrhi.mycoinkt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import kr.or.mrhi.mycoinkt.databinding.ActivityMainBinding
import kr.or.mrhi.mycoinkt.fragment.FragmentRss
import kr.or.mrhi.mycoinkt.fragment.Fragment_Coins
import kr.or.mrhi.mycoinkt.fragment.Fragment_Wallet
import kr.or.mrhi.mycoinkt.viewModel.CoinViewModel
import java.util.HashMap

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var model: CoinViewModel
    companion object {
        val stringSymbol = arrayOf(
            "BTC", "ETH", "BCH", "LTC", "BSV", "AXS", "BTG",
            "ETC", "DOT", "ATOM", "WAVES", "LINK", "REP", "OMG", "QTUM"
        )

        val stringName = arrayOf(
            "비트코인", "이더리움", "비트코인캐시", "라이트코인",
            "비트코인에스브이", "엑시인피니티", "비트코인골드", "이더리움클래식", "폴카닷", "코스모스", "웨이브",
            "체인링크", "어거", "오미세고", "퀀텀"
        )
        lateinit var namePositionMap: HashMap<String, Int>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val intent = intent
        val str = intent.getStringExtra("flag")
        if (str == null) {
            val intent2 = Intent(MainActivity@this, LoadingActivity::class.java)
            startActivity(intent2)
        }

        namePositionMap = HashMap<String, Int>()
        for (i in stringSymbol.indices) {
            namePositionMap.put(stringSymbol.get(i), i)
        }

        model = ViewModelProvider(this)[CoinViewModel::class.java]
        supportFragmentManager.beginTransaction().replace(R.id.main_frame, Fragment_Coins())
            .commit()
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->

            when (menuItem.itemId) {
                R.id.main -> {supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frame, Fragment_Coins()).commit()
                      model.stopThread()
                }
                R.id.news -> {supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frame, FragmentRss()).commit()
                    model.stopThread()
                }
                R.id.sub -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frame, Fragment_Wallet()).commit()
                    model.stopThread()
                }
                else -> finish()
            }
            true
        })
    }
}
