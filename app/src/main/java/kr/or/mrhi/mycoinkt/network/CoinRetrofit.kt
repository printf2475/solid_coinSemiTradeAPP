package kr.or.mrhi.mycoinkt.network

import kr.or.mrhi.mycoinkt.viewModel.CoinViewModel
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface CoinRetrofit {
    //{order_currency} = 주문 통화(코인), ALL(전체), 기본값 : BTC
    //{payment_currency} = 결제 통화(마켓), 입력값 : KRW 혹은 BTC
    @GET("candlestick/{order_currency}_{payment_currency}/{chart_intervals}")
    fun getCoinData(
        @Path("order_currency") order_currency: String?,
        @Path("payment_currency") payment_currency: String?,
        @Path("chart_intervals") chart_intervals: String?
    ): Call<CoinViewModel.NewCandleData?>?

    @GET("ticker/{order_currency}_{payment_currency}")
    fun getTickerCoinData(
        @Path("order_currency") order_currency: String?,
        @Path("payment_currency") payment_currency: String?
    ): Call<CoinViewModel.NewTickerData?>?

    @GET("ticker/{order_currency}_KRW")
    fun getTickerDTO(@Path("order_currency") order_currency: String?): Call<CoinViewModel.TickerData_single?>?

    @GET("transaction_history/{order_currency}_KRW")
    fun getTransactionCoinData(@Path("order_currency") order_currency: String?): Call<CoinViewModel.NewTransactionData?>?

    companion object {
        fun create(): CoinRetrofit? {
            val hostURL = "https://api.bithumb.com/public/"
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
                .create<CoinRetrofit>(CoinRetrofit::class.java)
        }
    }
}