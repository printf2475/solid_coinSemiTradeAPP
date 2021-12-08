package kr.or.mrhi.mycoinkt.network

import kr.or.mrhi.mycoinkt.POJO.History
import kr.or.mrhi.mycoinkt.viewModel.CoinViewModel
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface HistoryRetrofit {
    @GET("wallet/mobile/tradeHistory.php?coin=coins")
    fun getCoinHistory(): Call<History?>?

    companion object {
        fun createHistoryRetrofit(): HistoryRetrofit {
            val hostURL = "http://192.168.0.187/solid/"
            val pool = ConnectionPool(
                8,
                1000,
                TimeUnit.MILLISECONDS
            )
            val client: OkHttpClient = OkHttpClient.Builder()
                .connectionPool(pool)
                .build()

            return Retrofit.Builder()
                .baseUrl(hostURL) // 서버 호스트
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(HistoryRetrofit::class.java)
        }
    }
}