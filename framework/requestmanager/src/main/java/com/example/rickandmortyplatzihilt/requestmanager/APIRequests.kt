package com.example.rickandmortyplatzihilt.requestmanager

import com.example.rickandmortyplatzihilt.requestmanager.di.BaseUrlQualifier
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

abstract class BaseRequest<T: Any>(
    var baseUrl: String
) {

    private val okHttpClient: OkHttpClient = HttpLoggingInterceptor().run {
        level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder().addInterceptor(this).build()
    }

    inline fun <reified T:Any> getService(): T =
        buildRetrofit().run {
            create(T::class.java)
        }

    fun buildRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

@ViewModelScoped
class CharacterRequest @Inject constructor(
    @BaseUrlQualifier baseUrl: String
): BaseRequest<CharacterService>(baseUrl)

class EpisodeRequest @Inject constructor(
    @BaseUrlQualifier baseUrl: String
): BaseRequest<EpisodeService>(baseUrl)
