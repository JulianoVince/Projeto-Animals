package com.br.julianovincedecampos.animals

import com.br.julianovincedecampos.animals.di.ApiModule
import com.br.julianovincedecampos.animals.model.AnimalService

class ApiModuleTest(val mockService: AnimalService) : ApiModule() {

    override fun provideAnimalService(): AnimalService {
        return mockService
    }
}