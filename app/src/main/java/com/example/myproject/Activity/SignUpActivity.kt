package com.example.myproject.Activity

import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.myproject.Retrofit.RetrofitBuilder
import com.example.myproject.Retrofit.User
import com.example.myproject.databinding.ActivitySignupBinding
import com.example.myproject.prefs.App
import com.example.practiceapi.Retrofit.JsonplaceHolderApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding
    private var retrofitBuilder = RetrofitBuilder()
    private val year = arrayOf("1951","1952","1953","1954","1955","1956","1957","1958","1959","1960","1961","1962","1963","1964","1965","1966","1967","1968","1969"
        ,"1970","1971","1972","1973","1974","1975","1976","1977","1978","1979","1980","1981","1982","1983","1984","1985","1986","1987","1988","1989"
        ,"1990","1991","1992","1993","1994","1995","1996","1997","1998","1999","2000","2001","2002","2003","2004","2005","2006","2007","2008","2009","2010")
    private val month = arrayOf("01","02","03","04","05","06","07","08","09","10","11","12")
    private val day = arrayOf("01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24",
        "25","26","27","28","29","30")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpBtn.setOnClickListener {
            if(binding.editID.toString().isEmpty() && binding.editPW.toString().isEmpty()) {
                Toast.makeText(this, "?????? ????????? ???????????? ???????????????.", Toast.LENGTH_SHORT).show()
            }else {
                postUserData(
                    binding.editID.text.toString(),
                    binding.editPW.text.toString(),
                    binding.editEmail.text.toString(),
                    binding.birthText.text.toString()
                )
                Toast.makeText(this, "???????????? ??????", Toast.LENGTH_SHORT).show()
            }
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        var yearAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, year)
        binding.yearSpinner.adapter = yearAdapter
        binding.yearSpinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, p3: Long) {
                binding.birthText.setText(year.get(position))
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        var monthAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, month)
        binding.monthSpinner.adapter = monthAdapter
        binding.monthSpinner.onItemSelectedListener = object:AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, p3: Long) {
                binding.birthText.append(month.get(position))
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        var dayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, day)
        binding.daySpinner.adapter = dayAdapter
        binding.daySpinner.onItemSelectedListener = object:AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, p3: Long) {
                binding.birthText.append(day.get(position))
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        setSupportActionBar(binding.signUpToolbar)
        supportActionBar?.setTitle("sign Up")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }//end of onCreate

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                //toolbar??? back??? ????????? ??? ??????
                // ???????????? ??????
                var intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun postUserData(username: String, password :String, email: String, birth: String) {
        val jsonplaceHolderApi = retrofitBuilder.retrofit.create(
            JsonplaceHolderApi::class.java
        )
        Log.d("shin", "2???")
        val call = jsonplaceHolderApi.getUserPost(username, password, email, birth)
        call?.enqueue(object : Callback<ArrayList<User?>?> {
            override fun onResponse(
                call: Call<ArrayList<User?>?>,
                response: Response<ArrayList<User?>?>
            ) {
                binding.editID.setText("")
                binding.editPW.setText("")
                binding.editEmail.setText("")
            }

            override fun onFailure(call: Call<ArrayList<User?>?>, t: Throwable) {

            }
        })
    }//end of

}//end of Class