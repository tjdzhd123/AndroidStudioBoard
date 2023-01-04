package com.example.practiceapi.Retrofit

import com.example.myproject.Retrofit.Model
import com.example.myproject.Retrofit.User
import retrofit2.Call
import retrofit2.http.*
interface JsonplaceHolderApi {
    @get:GET("api/board/list")
    val data: Call<List<Model?>?>?

    @FormUrlEncoded
    @POST("api/board/new")
    fun createBoard(
        @Field("title") title:String,
        @Field("content") content:String,
        @Field("file_string") file_String:String,
        @Field("nickname") nickname:String
    ): Call<List<Model?>?>?

    @FormUrlEncoded
    @PATCH("api/board/{seq}")
    fun updateBoard(
        @Path("seq") seq:Int,
        @Field("title") title:String?,
        @Field("content") content:String?,
        @Field("nickname") nickname:String
    ): Call<List<Model?>?>?

    @DELETE("api/board/{seq}")
    fun deleteBoard(
        @Path("seq") seq:Int
    ): Call<List<Model?>?>?

    //========================================
    @FormUrlEncoded
    @POST("userApi/user")
    fun getUserPost(
        @Field("username") username: String?, @Field("password") password: String?,
        @Field("email") email: String?, @Field("birth") birth: String?
    ): Call<ArrayList<User?>?>?

    @FormUrlEncoded
    @POST("userApi/loginForm")
    fun getLoginUserPost(
        @Field("username") username: String?,
        @Field("password") password: String?
    ): Call<String?>?



}

