package kr.or.mrhi.mycoinkt.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kr.or.mrhi.myCoin.model.Transaction
import kr.or.mrhi.mycoinkt.DBController
import kr.or.mrhi.mycoinkt.MainActivity.Companion.namePositionMap
import kr.or.mrhi.mycoinkt.R
import kr.or.mrhi.mycoinkt.adapter.CoinListAdapter
import kr.or.mrhi.mycoinkt.databinding.FragmentMainBinding
import kr.or.mrhi.mycoinkt.databinding.FragmentTransacitemBinding
import kr.or.mrhi.mycoinkt.viewModel.CoinViewModel
import java.util.*

class Fragment_Coins : Fragment() {
    private var totalBuyCount = 0.0
    private var evaluationProfitCount = 0.0
    private lateinit var dbController: DBController
    private lateinit var model: CoinViewModel
    private lateinit var screenSlidePagerAdapter: CoinListAdapter
    private lateinit var transactionList: List<Transaction?>
    private lateinit var myCoinName: MutableList<String?>
    private lateinit var myCoinAmong: MutableList<String?>
    private lateinit var myCoinPrice: MutableList<String?>
    private lateinit var priceList: MutableList<String?>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding= FragmentMainBinding.inflate(LayoutInflater.from(context), container, false)



        model = ViewModelProvider(requireActivity()).get(CoinViewModel::class.java)
        dbController = DBController(requireActivity())
        transactionList = dbController.getMyWallet()
        myCoinName = ArrayList()

        for (transaction in transactionList) {
            myCoinName.add(transaction?.coinName)
        }
        model.getTransactionCoinData("BTC")
            ?.observe(requireActivity(), { currentData ->
                var transactionPrice = 0.0
                var buyCount = 0.0
                var buyPrice = 0.0
                var curruntPrice = 0.0
                priceList = ArrayList()
                myCoinAmong = ArrayList()
                myCoinPrice = ArrayList()
                totalBuyCount = 0.00
                evaluationProfitCount = 0.00
                //코인별 거래들을 각각 list에 넣음
                for (transaction in transactionList) {
                    myCoinAmong.add(transaction?.quantity!!)
                    myCoinPrice.add(transaction?.price!!)
                }
                for (price in myCoinPrice) {
                    transactionPrice += price?.toDouble()!!
                }

                //보유코인의 현재가격
                for (i in myCoinName.indices) {
                    if (namePositionMap.get(myCoinName.get(i)) != null) {
                        priceList.add(currentData[namePositionMap.get(myCoinName.get(i))!!])
                    }
                }
                if (!priceList.isEmpty() && transactionList!!.size != 0) {
                    for (i in priceList.indices) {
                        buyCount = myCoinAmong.get(i)?.toDouble()!!
                        buyPrice = myCoinPrice.get(i)?.toDouble()!!
                        curruntPrice = priceList.get(i)?.toDouble()!!
                        totalBuyCount += buyPrice //총매수
                        evaluationProfitCount += curruntPrice * buyCount //총평가
                    }
                    binding.tvTotalBuyCount.setText(String.format("%.0f", totalBuyCount)) //총매수
                    binding.tvTotalEvaluationCount.setText(
                        String.format(
                            "%.0f",
                            evaluationProfitCount
                        )
                    ) //총평가
                    binding.tvEvaluationProfitCount.setText(
                        String.format(
                            "%.0f",
                            evaluationProfitCount - totalBuyCount
                        )
                    )
                    binding.tvYieldCount.setText(
                        String.format(
                            "%.2f%%",
                            (evaluationProfitCount - totalBuyCount) / totalBuyCount * 100
                        )
                    )
                }
            })

        screenSlidePagerAdapter = CoinListAdapter(requireActivity())
        binding.edtTextSearchCoin.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                model.setSearchName(charSequence?.toString()?.toUpperCase())
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        binding.pager.adapter=screenSlidePagerAdapter
        val tabLayoutMediator =
            TabLayoutMediator(binding.tabLayout2, binding.pager, object :
                TabLayoutMediator.TabConfigurationStrategy {
                override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                    val textView = TextView(requireActivity())
                    textView.setGravity(Gravity.CENTER)
                    textView.setText(tabElement[position])
                    tab.setCustomView(textView)
                }
            })
        tabLayoutMediator.attach()
        return binding.root
    }

    override fun onPause() {
        super.onPause()

    }

    companion object {
        private val tabElement = arrayOf("전체", "관심")
    }
}