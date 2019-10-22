package com.elwaha.rawag.utilies

import com.elwaha.rawag.MyApp
import com.elwaha.rawag.data.storage.local.PreferencesHelper
import com.elwaha.rawag.data.storage.remote.RetrofitApiService
import com.elwaha.rawag.repo.CategoriesRepo
import com.elwaha.rawag.repo.LookupsRepo
import com.elwaha.rawag.repo.ProductsRepo
import com.elwaha.rawag.repo.UserRepo
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

    private fun createApi(client: OkHttpClient): RetrofitApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .client(client)
            .build()

        return retrofit.create(RetrofitApiService::class.java)
    }

    fun getApplicationContext() = MyApp.instance

    private fun getApiService() = createApi(getOkHttpClient())

    fun getPreferenceHelper() = PreferencesHelper(getApplicationContext())

    //========================================Repo================================================

    fun getCategoriesRepo() = CategoriesRepo(getApiService())
    fun getUserRepo() = UserRepo(getApiService())
    fun getProductsRepo() = ProductsRepo(getApiService())
    fun getLookupsRepo() = LookupsRepo(getApiService())

}