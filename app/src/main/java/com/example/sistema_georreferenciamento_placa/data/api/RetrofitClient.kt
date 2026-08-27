package com.example.sistema_georreferenciamento_placa.data.api

import PlateApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://SEU_IP_AQUI:8000/"

    val api: PlateApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlateApi::class.java)
    }
}