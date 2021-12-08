package kr.or.mrhi.mycoinkt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kr.or.mrhi.mycoinkt.network.LoginRetrofit
import kr.or.mrhi.mycoinkt.network.LoginRetrofit.Companion.createLoginRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class login_Activity : AppCompatActivity() {
    lateinit var btn_login_kakao: Button
    lateinit var btn_login: Button
    lateinit var loginRetrofit:LoginRetrofit
    lateinit var edt_id : EditText
    lateinit var edt_pwd : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        btn_login_kakao = findViewById<Button>(R.id.btn_login_kakao)
        btn_login = findViewById<Button>(R.id.btn_login)
        edt_id = findViewById(R.id.edt_ID);
        edt_pwd = findViewById(R.id.edt_pwd);

        loginRetrofit=createLoginRetrofit()
        btn_login.setOnClickListener {
            loginRetrofit.getUserLogin(edt_id.text.toString(), edt_pwd.text.toString(), "mobile")?.enqueue(
                object : Callback<String?> {
                    override fun onResponse(call: Call<String?>, response: Response<String?>) {
                        if (response.body().toString() =="false") {
                            Log.e("로그인", "로그인 실패!")
                        } else if (response.body().toString() =="true") {
                            Log.e("로그인", "로그인 성공!")
                            it.context.startActivity(intent)
                        }
                    }

                    override fun onFailure(call: Call<String?>, t: Throwable) {
                        Log.i("로그인.PHP", "실패"+t.message)
                    }
                })
        }

        btn_login_kakao.setOnClickListener {

            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.e("로그인", "로그인 실패!", error)
                } else if (token != null) {
                    Log.e("로그인", "로그인 성공! ${token.accessToken}")

                    it.context.startActivity(intent)
                }
            }

            Log.e("로그인", "시도")
            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(it.context)) {
                UserApiClient.instance.loginWithKakaoTalk(it.context, callback = callback)
            } else {
                UserApiClient.instance.loginWithKakaoAccount(it.context, callback = callback)
            }
        }


    }



}
