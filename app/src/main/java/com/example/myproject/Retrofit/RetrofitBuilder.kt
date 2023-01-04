package com.example.myproject.Retrofit

import com.example.myproject.prefs.App
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitBuilder {
    private fun provideOkHttpClient(
    ): OkHttpClient = OkHttpClient.Builder()
        .run {
            addInterceptor(interceptor)
            build()
        }

    val interceptor = Interceptor { chain ->
        with(chain) {
            val newRequest = request().newBuilder()
                .addHeader("Authorization", "Bearer " + App.prefs.getString("token",""))
                .build()
            proceed(newRequest)
        }
    }//interceptor

    val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.0.2:8081/")
        .client(provideOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}