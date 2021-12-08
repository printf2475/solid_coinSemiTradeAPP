package kr.or.mrhi.mycoinkt.adapter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kr.or.mrhi.mycoinkt.fragment.Fragment_AllCoins
import kr.or.mrhi.mycoinkt.fragment.Fragment_LikeCoin
import androidx.fragment.app.Fragment as Fragment

class CoinListAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    var searchName: String? = null
    lateinit var fragment1: Fragment
    lateinit var fragment2: Fragment
    override fun createFragment(position: Int): Fragment {

        return when(position) {
            0->{
                fragment1 = Fragment_AllCoins()
                if (searchName != null) {
                    val bundle = Bundle()
                    bundle.putString("searchName", searchName)
                    fragment1!!.arguments = bundle
                }
                fragment1
            }
            1->{
                    fragment2 = Fragment_LikeCoin()
                    if (searchName != null) {
                        val bundle = Bundle()
                        bundle.putString("searchName", searchName)
                        fragment2!!.arguments = bundle
                    }
                    fragment2
            }
            else -> {
                Log.d("프레그먼트", "createFragment() 프레그먼트 생성 오류")
               fragment1
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
