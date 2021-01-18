package com.br.julianovincedecampos.animals.di

import com.br.julianovincedecampos.animals.model.AnimalService
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent {

    fun inject(service: AnimalService)

}