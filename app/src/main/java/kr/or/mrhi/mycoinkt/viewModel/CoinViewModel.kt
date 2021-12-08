package kr.or.mrhi.mycoinkt.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kr.or.mrhi.mycoinkt.MainActivity.Companion.namePositionMap
import kr.or.mrhi.mycoinkt.MainActivity.Companion.stringSymbol
import kr.or.mrhi.mycoinkt.POJO.CandleCoinData
import kr.or.mrhi.mycoinkt.POJO.TickerDataClass
import kr.or.mrhi.mycoinkt.POJO.TransactionData
import kr.or.mrhi.mycoinkt.model.TickerDTO
import kr.or.mrhi.mycoinkt.network.CoinRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class CoinViewModel : ViewModel() {
    private val LATELYDATA = 19

    private var candleCoinData: MutableLiveData<List<CandleCoinData>>? = null
    private var tickerCoinData: MutableLiveData<List<TickerDTO>>? = null
    private var transactionCoinData: MutableLiveData<List<String>>? = null
    private var searchName: MutableLiveData<String>
    private var newCandleData: NewCandleData
    private var newTickerData: NewTickerData
    private var newTransactionData: NewTransactionData
    private var tickerDatasingle: TickerData_single
    private var priceList: MutableList<String>
    private var stopFlag: Boolean = false
    private var retrofit: CoinRetrofit

    init {
        retrofit = CoinRetrofit.create()!!
        searchName = MutableLiveData<String>()
        newCandleData = NewCandleData()
        newTickerData = NewTickerData()
        newTransactionData = NewTransactionData()
        tickerDatasingle = TickerData_single()
        priceList = ArrayList(20)
        for (i in 0 until stringSymbol.size) {
            priceList.add("0.00")
        }
        stopFlag = false
    }


    fun getSearchName(): MutableLiveData<String> {
        searchName.value = ""
        return searchName
    }

    fun setSearchName(searchName: String?) {
        this.searchName.value = searchName
    }

    fun getCandleCoinData(
        coinName: String,
        intervals: String?
    ): MutableLiveData<List<CandleCoinData>>? {
        if (candleCoinData == null) {
            candleCoinData = MutableLiveData<List<CandleCoinData>>()
        }
        newCandleData.refreshCoinData(coinName, intervals)
        return candleCoinData
    }

    fun getTickerCoinData(): MutableLiveData<List<TickerDTO>>? {
        if (tickerCoinData == null) {
            tickerCoinData = MutableLiveData<List<TickerDTO>>()
        }
        newTickerData.refreshCoinData()
        return tickerCoinData
    }

    fun getTickerDTO(coinName: String): MutableLiveData<List<TickerDTO>>? {
        if (tickerCoinData == null) {
            tickerCoinData = MutableLiveData<List<TickerDTO>>()
        }
        tickerDatasingle.refreshTickerDTO(coinName)
        return tickerCoinData
    }

    fun getTransactionCoinData(coinName: String): MutableLiveData<List<String>>? {
        if (transactionCoinData == null) {
            transactionCoinData = MutableLiveData<List<String>>()
        }
        newTransactionData.refreshTransactionCoinData(coinName)
        return transactionCoinData
    }

    fun refrashTransactionDataThread() {
        stopFlag = true
        val thread = Thread {
            while (stopFlag) {
                for (i in stringSymbol.indices) {
                    synchronized(this) { newTransactionData.refreshTransactionCoinData(stringSymbol[i]) }
                }
                try {
                    Thread.sleep(500)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
        thread.isDaemon = true
        thread.start()
    }

    fun stopThread() {
        stopFlag = false
    }

    inner class NewCandleData {
        @SerializedName("status")
        @Expose
        private val status: String? = null

        @SerializedName("data")
        @Expose
        private val data: List<List<String>>? = null
        fun refreshCoinData(coinName: String, intervals: String?) {
            retrofit.getCoinData(coinName.toUpperCase(), "KRW", intervals)?.enqueue(object : Callback<NewCandleData?> {
                override fun onResponse(
                    call: Call<NewCandleData?>,
                    response: Response<NewCandleData?>
                ) {
                    candleCoinData?.setValue(makeCoinList(response))
                }

                override fun onFailure(call: Call<NewCandleData?>, t: Throwable) {
                    Log.i("이전코인", "실패 : " + t.fillInStackTrace())
                }

            })
        }


        private fun makeCoinList(response: Response<NewCandleData?>): List<CandleCoinData> {
            val formerCoinList: MutableList<CandleCoinData> = ArrayList<CandleCoinData>()
            for (list in response.body()!!.data!!) {
                val coin = CandleCoinData(list[0], list[1], list[2], list[3], list[4], list[5])
                formerCoinList.add(coin)
            }
            Log.i("이전코인", formerCoinList.size.toString() + "")
            return formerCoinList
        }
    }

    inner class NewTickerData {
        @SerializedName("status")
        @Expose
        private val status: String? = null

        @SerializedName("data")
        @Expose
        private val tickerDataClass: TickerDataClass? = null
        fun refreshCoinData() {
            retrofit.getTickerCoinData("ALL", "KRW")?.enqueue(object : Callback<NewTickerData?> {
                override fun onResponse(
                    call: Call<NewTickerData?>,
                    response: Response<NewTickerData?>
                ) {
                    tickerCoinData?.setValue(response.body()!!.newData())
                }

                override fun onFailure(call: Call<NewTickerData?>, t: Throwable) {
                    Log.i("현재코인", "실패 : " + t.fillInStackTrace())
                }

            })
        }



        fun newData(): List<TickerDTO> {

            val list: MutableList<TickerDTO> = ArrayList<TickerDTO>()
            list.add(tickerDataClass?.btc!!)
            list.add(tickerDataClass?.eth!!)
            list.add(tickerDataClass?.bch!!)
            list.add(tickerDataClass?.ltc!!)
            list.add(tickerDataClass?.bsv!!)
            list.add(tickerDataClass?.axs!!)
            list.add(tickerDataClass?.btg!!)
            list.add(tickerDataClass?.etc!!)
            list.add(tickerDataClass?.dot!!)
            list.add(tickerDataClass?.atom!!)
            list.add(tickerDataClass?.waves!!)
            list.add(tickerDataClass?.link!!)
            list.add(tickerDataClass?.rep!!)
            list.add(tickerDataClass?.omg!!)
            list.add(tickerDataClass?.qtum!!)
            return list

        }
    }

    inner class NewTransactionData {
        @SerializedName("status")
        @Expose
        private val status: String? = null

        @SerializedName("data")
        @Expose
        private val data: List<TransactionData>? = null
        fun refreshTransactionCoinData(coinName: String) {
            retrofit.getTransactionCoinData(coinName)?.enqueue(object : Callback<NewTransactionData?> {
                    override fun onResponse(
                        call: Call<NewTransactionData?>,
                        response: Response<NewTransactionData?>
                    ) {
                        transactionCoinData?.setValue(makeMapData(coinName, response))
                    }

                    override fun onFailure(call: Call<NewTransactionData?>, t: Throwable) {
                        Log.i("현재코인", "실패 : " + t.fillInStackTrace())
                    }
                })
        }

        private fun makeMapData(
            coinName: String,
            response: Response<NewTransactionData?>
        ): List<String> {
            if (response.body() != null) {
                priceList[namePositionMap.get(coinName)!!] =
                    response.body()!!.data!![LATELYDATA].price!!
            }
            return priceList
        }


    }

    inner class TickerData_single {
        @SerializedName("status")
        @Expose
        private val status: String? = null

        @SerializedName("data")
        @Expose
        val data: TickerDTO? = null


        fun refreshTickerDTO(coinName: String) {
            retrofit.getTickerDTO(coinName)?.enqueue(object : Callback<TickerData_single?> {
                override fun onResponse(
                    call: Call<TickerData_single?>,
                    response: Response<TickerData_single?>
                ) {
                    tickerCoinData?.value = getData(response)
                }

                override fun onFailure(call: Call<TickerData_single?>, t: Throwable) {
                    Log.i("현재코인", "실패 : " + t.fillInStackTrace())
                }

            })
        }

        private fun getData(response: Response<TickerData_single?>): List<TickerDTO>? {
            var list : ArrayList<TickerDTO> = arrayListOf()
            list.add(response.body()?.data!!)
            return list
        }
    }


}



