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
import kr.or.mrhi.mycoinkt.DBController
import kr.or.mrhi.mycoinkt.POJO.History
import kr.or.mrhi.mycoinkt.POJO.HistoryItem
import kr.or.mrhi.mycoinkt.R
import kr.or.mrhi.mycoinkt.adapter.TransactionHistoryAdapter
import kr.or.mrhi.mycoinkt.network.HistoryRetrofit
import kr.or.mrhi.mycoinkt.viewModel.CoinViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Fragment_transactionHistory : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var controller: DBController
    private lateinit var model: CoinViewModel
    private lateinit var transactionList: ArrayList<HistoryItem>
    private lateinit var historyRetrofit: HistoryRetrofit
    private lateinit var adapter :TransactionHistoryAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_transaction_history, container, false)
        transactionList=ArrayList<HistoryItem>()
        model = ViewModelProvider(requireActivity()).get(CoinViewModel::class.java)
        model.stopThread()
        recyclerView = view.findViewById(R.id.transactionRecycler)
        recyclerView.layoutManager=LinearLayoutManager(requireActivity())
        adapter = TransactionHistoryAdapter(transactionList)
        recyclerView.adapter=adapter

        historyRetrofit= HistoryRetrofit.createHistoryRetrofit()
        historyRetrofit.getCoinHistory()?.enqueue(object : Callback<History?> {
            override fun onResponse(call: Call<History?>, response: Response<History?>) {
                transactionList.addAll(response.body()!!)
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<History?>, t: Throwable) {
                Log.i("거래내역", "실패"+t.message)
            }
        })



        return view
    }


}