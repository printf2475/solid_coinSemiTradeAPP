package kr.or.mrhi.mycoinkt.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kr.or.mrhi.mycoinkt.DBController
import kr.or.mrhi.mycoinkt.R
import kr.or.mrhi.mycoinkt.adapter.WalletTabLayoutAdapter

class Fragment_Wallet : Fragment(){
    private lateinit var tabLayout2: TabLayout
    private lateinit var pager2: ViewPager2
    private lateinit var dbController: DBController
    private lateinit var tabLayoutAdapter: WalletTabLayoutAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_wallet, container, false)
        // Inflate the layout for this fragment
        pager2 = view.findViewById(R.id.pager2)
        tabLayout2 = view.findViewById(R.id.tabLayout2)
        dbController = DBController(requireActivity())
        tabLayoutAdapter = WalletTabLayoutAdapter(activity as FragmentActivity)
        pager2.adapter=tabLayoutAdapter
        val tabLayoutMediator =
            TabLayoutMediator(tabLayout2, pager2, object :
                TabLayoutMediator.TabConfigurationStrategy {
                override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                    val textView = TextView(requireActivity())
                    textView.gravity=Gravity.CENTER
                    textView.text=tabElement[position]
                    tab.customView=textView
                }
            })
        tabLayoutMediator.attach()
        return view
    }


    companion object {
        private val tabElement = arrayOf("보유코인", "거래내역")
    }
}