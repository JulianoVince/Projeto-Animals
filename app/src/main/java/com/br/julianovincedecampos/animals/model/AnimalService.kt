package com.br.julianovincedecampos.animals.model

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AnimalService {

    private val BASE_URL = "https://us-centrall-apis-4674e.cloudfunctions.net"

    private val api =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(AnimalApi::class.java)

    fun getApiKey(): Single<ApiKey> {
        return api.getApiKey()
    }

    fun getAnimals(key: String): Single<List<Animal>> {
        return api.getAnimals(key)
    }

}