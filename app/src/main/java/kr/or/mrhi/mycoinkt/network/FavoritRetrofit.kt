package kr.or.mrhi.mycoinkt.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.POST

import retrofit2.http.FormUrlEncoded
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.scalars.ScalarsConverterFactory


interface FavoritRetrofit {

    @FormUrlEncoded
    @POST("coinList/DB/favorit.php")
    fun getFavoritCoin(
        @Field("client") client: String?
    ): Call<ArrayList<String>>?
//ArrayList<String>
    companion object{
        fun createFavoritRetrofit(): FavoritRetrofit {
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
                .create(FavoritRetrofit::class.java)
        }
    }
}