package com.br.julianovincedecampos.animals.model

import com.br.julianovincedecampos.animals.di.DaggerApiComponent
import io.reactivex.Single
import javax.inject.Inject

class AnimalService {

    @Inject
    lateinit var api: AnimalApi

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun getApiKey(): Single<ApiKey> {
        return api.getApiKey()
    }

    fun getAnimals(key: String): Single<List<Animal>> {
        return api.getAnimals(key)
    }

}