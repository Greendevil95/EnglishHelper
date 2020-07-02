package com.example.laba2

//package com.example.laba2.provider
//
//import android.app.Application
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//
//class App : Application() {
//    private var retrofit: Retrofit? = null
//    private var appApi: AppApi? = null
//    override fun onCreate() {
//        super.onCreate()
//        retrofit = Retrofit.Builder()
//            .baseUrl("https://api.github.com/") //Базовая часть адреса
//            .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
//            .build()
//        appApi =
//            retrofit?.create<AppApi>(AppApi::class.java) //Создаем объект, при помощи которого будем выполнять запросы
//    }
//
//    fun getApi(): AppApi? {
//        return appApi
//    }
//}