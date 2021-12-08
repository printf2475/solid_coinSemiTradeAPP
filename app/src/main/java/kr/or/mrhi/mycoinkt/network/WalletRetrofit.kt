package kr.or.mrhi.mycoinkt.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kr.or.mrhi.mycoinkt.POJO.WalletData
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

interface WalletRetrofit {
    @GET("wallet/mobile/walletList.php")
    fun getWallet(): Call<WalletData?>?

    companion object {
        fun createWalletRetrofit(): WalletRetrofit {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
            val gson : Gson = GsonBuilder().setLenient().create()
            val hostURL = "http://192.168.0.187/solid/"
            return Retrofit.Builder()
                .baseUrl(hostURL) // 서버 호스트
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create() )
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(WalletRetrofit::class.java)
        }
    }
}