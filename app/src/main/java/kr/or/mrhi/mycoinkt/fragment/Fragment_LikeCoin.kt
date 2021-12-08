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
import kr.or.mrhi.mycoinkt.adapter.FavoritCoinAdapter
import kr.or.mrhi.mycoinkt.model.TickerDTO
import kr.or.mrhi.mycoinkt.network.DBController
import kr.or.mrhi.mycoinkt.network.FavoritRetrofit
import kr.or.mrhi.mycoinkt.viewModel.CoinViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Fragment_LikeCoin : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var likeCoinAdapter: FavoritCoinAdapter
    private lateinit var priceList: MutableList<String>
    private lateinit var dbController: DBController
    private lateinit var favoriteList: ArrayList<String>
    private lateinit var favoriteListTemp: MutableList<String>
    private lateinit var model: CoinViewModel
    private lateinit var loginRetrofit: FavoritRetrofit
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_favorit, container, false)
        model = ViewModelProvider(requireActivity()).get(CoinViewModel::class.java)
        dbController = DBController(requireActivity())
        priceList = ArrayList()
        favoriteList = ArrayList()
        favoriteListTemp = ArrayList()

        priceList.removeAll(priceList)
        recyclerView = view.findViewById(R.id.recyclerView1)
        recyclerView.setLayoutManager(LinearLayoutManager(requireActivity()))
        likeCoinAdapter = FavoritCoinAdapter(priceList, favoriteListTemp)
        recyclerView.setAdapter(likeCoinAdapter)
        loginRetrofit= FavoritRetrofit.createFavoritRetrofit()



        loginRetrofit.getFavoritCoin("mobile")?.enqueue(object : Callback<ArrayList<String>> {
            override fun onResponse(call: Call<ArrayList<String>>,
                response: Response<ArrayList<String>>) {
                for (i in response.body()!!){
                    if (stringSymbol.contains(i)){
                        favoriteList.add(i)
                    }
                }

                favoriteListTemp.addAll(favoriteList)
                likeCoinAdapter.notifyDataSetChanged()

                model.getTransactionCoinData("BTC")
                    ?.observe(requireActivity()){ transactionData ->
                        priceList.removeAll(priceList)

                        if (!favoriteListTemp.isEmpty()) {
                            for (i in favoriteListTemp.indices) {
                                priceList.add(transactionData[namePositionMap.get(favoriteListTemp.get(i))?: 0])
                            }
                        }
                        likeCoinAdapter.notifyDataSetChanged()
                    }

                model.getSearchName().observe(viewLifecycleOwner){ name ->
                    favoriteListTemp.removeAll(favoriteListTemp)
                    for (i in favoriteList.indices) {
                        if (favoriteList[i].contains(name!!)) {
                            favoriteListTemp.add(favoriteList[i])
                        }
                    }
                    likeCoinAdapter.notifyDataSetChanged()
                }
                model.refrashTransactionDataThread()
            }

            override fun onFailure(call: Call<ArrayList<String>>, t: Throwable) {
               Log.i("즐겨찾기", "가져오기 실패"+t.message)
            }
        })

        model.getTickerCoinData()?.observe(requireActivity()){ tickerData ->
                var tickerDataList : ArrayList<TickerDTO> = ArrayList<TickerDTO>()
                for (i in favoriteList.indices){
                    tickerDataList.add(tickerData[namePositionMap.get(favoriteListTemp.get(i))?: 0])
                }

                likeCoinAdapter.setTickerData(tickerDataList)
                likeCoinAdapter.notifyDataSetChanged()
            }


        return view
    }

}


