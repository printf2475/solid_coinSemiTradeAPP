package kr.or.mrhi.mycoinkt.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.or.mrhi.mycoinkt.MainActivity.Companion.namePositionMap
import kr.or.mrhi.mycoinkt.MainActivity.Companion.stringSymbol
import kr.or.mrhi.mycoinkt.R
import kr.or.mrhi.mycoinkt.adapter.MainCoinAdapter
import kr.or.mrhi.mycoinkt.viewModel.CoinViewModel
import java.util.ArrayList


class Fragment_AllCoins : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var mainCoinAdapter: MainCoinAdapter
    private lateinit var priceList: MutableList<String>
    private lateinit var searchList: MutableList<String>
    private lateinit var model: CoinViewModel
    private var count =0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_fragment1_2, container, false)
        // Inflate the layout for this fragment
        model = ViewModelProvider(requireActivity()).get(CoinViewModel::class.java)
        priceList = ArrayList()
        searchList = ArrayList()
        recyclerView = view.findViewById(R.id.recyclerView2)
        recyclerView.layoutManager=LinearLayoutManager(requireActivity())
        recyclerView.setNestedScrollingEnabled(false);
        mainCoinAdapter = MainCoinAdapter(priceList, searchList)
        recyclerView.adapter=mainCoinAdapter

        model.getTickerCoinData()?.observe(requireActivity(), { tickerData ->
            mainCoinAdapter.setTickerData(tickerData)
            mainCoinAdapter.notifyDataSetChanged()
        })


        model.getTransactionCoinData("BTC")
            ?.observe(requireActivity(), { transactionData ->
                count++
                priceList.removeAll(priceList)
                if (searchList.size != 0) {
                    for (i in searchList.indices) {
                        priceList.add(transactionData[namePositionMap.get(searchList.get(i))?:0])
                    }
                } else {
                    priceList.addAll(transactionData)
                }
                if (count===50){
                    mainCoinAdapter.notifyDataSetChanged()
                    count=0
                }
            })
        model.refrashTransactionDataThread()

        model.getSearchName().observe(viewLifecycleOwner,{ searchName ->
            searchList.removeAll(searchList)
            for (i in stringSymbol.indices) {
                if (stringSymbol.get(i).contains(searchName)) {
                    searchList.add(stringSymbol.get(i))
                }
            }
            mainCoinAdapter.notifyDataSetChanged()
        })
        return view
    }

}