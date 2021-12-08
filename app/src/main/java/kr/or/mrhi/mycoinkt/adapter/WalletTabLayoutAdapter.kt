package kr.or.mrhi.mycoinkt.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kr.or.mrhi.mycoinkt.fragment.Fragment_transactionHistory
import kr.or.mrhi.mycoinkt.fragment.OwnBank

class WalletTabLayoutAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> OwnBank()
            1->  Fragment_transactionHistory()
            else -> {
                Log.d("프레그먼트", "createFragment() 프레그먼트 생성 오류")
                OwnBank()
            }
        }
    }

    //뷰페이지 개수
    override fun getItemCount(): Int {
        return NUM_PAGES
    }

    companion object {
        private const val NUM_PAGES = 2
    }
} //end of adapter
