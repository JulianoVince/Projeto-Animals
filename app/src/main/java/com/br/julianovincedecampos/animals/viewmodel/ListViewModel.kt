package com.br.julianovincedecampos.animals.viewmodel

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.br.julianovincedecampos.animals.di.AppModule
import com.br.julianovincedecampos.animals.di.CONTEXT_APP
import com.br.julianovincedecampos.animals.di.DaggerViewModelComponent
import com.br.julianovincedecampos.animals.di.TypeOfContext
import com.br.julianovincedecampos.animals.model.Animal
import com.br.julianovincedecampos.animals.model.AnimalService
import com.br.julianovincedecampos.animals.model.ApiKey
import com.br.julianovincedecampos.animals.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListViewModel(application: Application) : AndroidViewModel(application) {

    constructor(application: Application, test: Boolean = true) : this(application) {
        inject = true
    }

    val animals by lazy { MutableLiveData<List<Animal>>() }
    val loadError by lazy { MutableLiveData<Boolean>() }
    val loading by lazy { MutableLiveData<Boolean>() }

    private val disposable = CompositeDisposable()

    @Inject
    lateinit var apiService: AnimalService

    @Inject
    @field:TypeOfContext(CONTEXT_APP)
    lateinit var prefs: SharedPreferencesHelper

    fun inject() {
        if (!inject) {
            DaggerViewModelComponent.builder().appModule(AppModule(getApplication())).build()
                .inject(this)
        }
    }

    private var invalidApiKey = false
    private var inject = false

    fun refresh() {
        inject()
        invalidApiKey = false
        loading.value = true
        val key = prefs.getApiKey()

        if (key.isNullOrEmpty()) {
            getKey()
        } else {
            getAnimals(key)
        }
    }

    fun hardRefresh() {
        inject()
        loading.value = true
        getKey()
    }

    private fun getKey() {
        disposable.add(
            apiService.getApiKey()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ApiKey>() {
                    override fun onSuccess(keyValue: ApiKey) {
                        if (keyValue.key.isNullOrEmpty()) {
                            loadError.value = true
                            loading.value = false
                        } else {
                            prefs.saveApiKey(key = keyValue.key)
                            getAnimals(key = keyValue.key)
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        loading.value = false
                        loadError.value = true
                    }
                })
        )
    }


    fun getAnimals(key: String) {
        disposable.add(
            apiService.getAnimals(key)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Animal>>() {

                    override fun onSuccess(listBackEnd: List<Animal>) {
                        loadError.value = false
                        animals.value = listBackEnd
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        if (!invalidApiKey) {
                            invalidApiKey = true
                            getKey()
                        } else {
                            e.printStackTrace()
                            loading.value = false
                            animals.value = null
                            loadError.value = true
                        }
                    }
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}