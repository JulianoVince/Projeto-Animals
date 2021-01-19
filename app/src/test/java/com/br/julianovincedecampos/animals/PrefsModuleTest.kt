package com.br.julianovincedecampos.animals

import android.app.Application
import com.br.julianovincedecampos.animals.di.PrefsModule
import com.br.julianovincedecampos.animals.util.SharedPreferencesHelper

class PrefsModuleTest(val mockPrefs: SharedPreferencesHelper) : PrefsModule() {

    override fun providesSharedPreferences(app: Application): SharedPreferencesHelper {
        return mockPrefs
    }
}