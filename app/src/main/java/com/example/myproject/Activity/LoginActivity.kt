package com.example.myproject.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myproject.R
import com.example.myproject.databinding.ActivityLoginBinding
import com.example.myproject.prefs.App
import com.example.practiceapi.Retrofit.JsonplaceHolderApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.logging.Logger

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var logger: Logger
    val gson : Gson = GsonBuilder()
        .setLenient()
        .create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        logger = Logger.getLogger("App Info")

        //로그인 버튼 누를 때
        binding.loginBtn.setOnClickListener {
            getLoginUserPost(
                binding.loginID.text.toString(),
                binding.loginPassWord.text.toString(),
            )
        }

        //회원 가입 버튼 누를 때
        binding.signUpBtn.setOnClickListener {
            var intent = Intent (this, SignUpActivity::class.java)
            startActivity(intent)
        }

    }//end of onCreate
    //로그인시 게시판 리스트 가져오기
    private fun getLoginUserPost(username: String, password:String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.2:8081/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val jsonplaceHolderApi = retrofit.create(
            JsonplaceHolderApi::class.java
        )
        val call = jsonplaceHolderApi.getLoginUserPost(username, password)
        call?.enqueue(object : Callback<String?> {
            override fun onResponse(
                call: Call<String?>,
                response: Response<String?>
            ) {
                logger.info("${response.body()}")
                Log.d("shin", "${response.body()}")
                binding.loginID.setText("")
                binding.loginPassWord.setText("")
                App.prefs.setString("token", "${response.body()}")
                logger.info("token ::${App.prefs.getString("token", "").toString()}")
                var intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
            }
            override fun onFailure(call: Call<String?>, t: Throwable) {
            }
        })
    }//end of getLoginUserPost
}//end of class