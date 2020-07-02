package com.example.laba2

//package com.example.laba2.provider
//
//import com.google.gson.internal.LinkedTreeMap
//import retrofit2.Call
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.http.GET
//import retrofit2.http.Headers
//import retrofit2.http.POST
//import retrofit2.http.Query
//
//public interface AppApi {
//
//    @Headers("content-type: application/json")
//    @POST(".")
//    fun getDecks(): Call<LinkedTreeMap<String, Any>>
//
//    @POST
//    fun getCards(): Call<LinkedTreeMap<String, Any>>
//
//    companion object {
//        fun create(): AppApi {
//            val retrofit = Retrofit.Builder()
//                .baseUrl("https://api.jsonbin.io/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//
//            return retrofit.create(AppApi::class.java)
//        }
//    }
//
//
//}