package com.example.myproject.Activity

import android.R
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myproject.Recycler.RecyclerViewAdapter
import com.example.myproject.Recycler.RecyclerViewDeco
import com.example.myproject.Recycler.WritingData
import com.example.myproject.Retrofit.Model
import com.example.myproject.Retrofit.RetrofitBuilder
import com.example.myproject.databinding.ActivityMainBinding
import com.example.myproject.prefs.App
import com.example.practiceapi.Retrofit.JsonplaceHolderApi
import lombok.Data
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var isFabOpen = false
    lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private var retrofitBuilder = RetrofitBuilder()
    var writingList: MutableList<WritingData> = mutableListOf<WritingData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        boardRetrofit()
        super.onCreate(savedInstanceState)
        this.binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //툴바 설정하기
        setSupportActionBar(binding.boardToolbar)
        supportActionBar?.setTitle("my Diary")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //토글이벤트
        binding.fabMain.setOnClickListener {
            toggleFab()
        }
        //플로팅버튼 이벤트
        binding.fabAdd.setOnClickListener {
            var intent = Intent(this, WritingActivity::class.java)
            startActivity(intent)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }



    } //end of onCreate


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                //toolbar의 back키 눌렀을 때 동작
                // 액티비티 이동
                var intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun toggleFab() {
        if (isFabOpen) {
            ObjectAnimator.ofFloat(binding.fabAdd, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(binding.fabMain, View.ROTATION, 45f, 0f).apply { start() }
        } else { // 플로팅 액션 버튼 열기 - 닫혀있는 플로팅 버튼 꺼내는 애니메이션
            ObjectAnimator.ofFloat(binding.fabAdd, "translationY", -250f).apply { start() }
            ObjectAnimator.ofFloat(binding.fabMain, View.ROTATION, 0f, 45f).apply { start() }
        }
        isFabOpen = !isFabOpen
    }

    //Retrofit Get
    fun boardRetrofit() {
        //Retrofit Builder
        // instance for interface
        val jsonplaceHolderApi = retrofitBuilder.retrofit.create(
            JsonplaceHolderApi::class.java
        )
        val call = jsonplaceHolderApi.data
        call?.enqueue(object : Callback<List<Model?>?> {
            override fun onResponse(call: Call<List<Model?>?>, response: Response<List<Model?>?>) {
                val models = response.body()!!
                    for(model in models) {
                        val seq = model?.seq
                        val name = model?.nickname
                        val title = model?.title
                        val content = model?.content
                        var dateStr = model?.dt

                    val writingData = WritingData(seq, name, title, content, dateStr)
                    writingList.add(writingData)
                    recyclerViewAdapter = RecyclerViewAdapter(this@MainActivity, writingList)
                    binding.recyclerView.adapter = recyclerViewAdapter
                    binding.recyclerView.addItemDecoration(RecyclerViewDeco(this@MainActivity))
                    binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

                }
            }
            override fun onFailure(call: Call<List<Model?>?>, t: Throwable) {
                Toast.makeText(this@MainActivity, "데이터를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }//end of retrofit



} //end of MainActivity