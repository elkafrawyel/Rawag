package com.elwaha.rawag.utilies

import com.elwaha.rawag.MyApp
import com.elwaha.rawag.data.storage.local.PreferencesHelper
import com.elwaha.rawag.data.storage.remote.CoroutinesRetrofitApiService
import com.elwaha.rawag.data.storage.remote.RetrofitApiService
import com.elwaha.rawag.utilies.Constants.BASE_URL
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object Injector {

    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private fun getApiServiceHeader(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .addHeader("Accept", "application/json")

            if (chain.request().header("Accept-Language") == null) {
                request.addHeader(
                    "Accept-Language",
                    chain.request().header("Accept-Language") ?: getPreferenceHelper().language!!
                )
            }
            chain.proceed(request.build())
        }
    }

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .addInterceptor(getApiServiceHeader())
            .addInterceptor(getLoggingInterceptor())
            .build()
    }

    private fun createCoroutinesApi(client: OkHttpClient): CoroutinesRetrofitApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .client(client)
            .build()

        return retrofit.create(CoroutinesRetrofitApiService::class.java)
    }

    private fun createApi(client: OkHttpClient): RetrofitApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(RetrofitApiService::class.java)
    }

    private fun getCoroutinesApiService() =
        createCoroutinesApi(getOkHttpClient())

    private fun getApiService() =
        createApi(getOkHttpClient())

    fun getApplicationContext() = MyApp.instance

    fun getPreferenceHelper() =
        PreferencesHelper(getApplicationContext())

    //========================================Repo================================================


}