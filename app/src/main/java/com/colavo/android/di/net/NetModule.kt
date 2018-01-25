package com.colavo.android.di.net

import com.colavo.android.R.id.password
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Singleton
import okhttp3.OkHttpClient


@Module
class NetModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
            .setLenient()
            .excludeFieldsWithoutExposeAnnotation()
            .create()

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson): Retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
               // .client(okHttpClient)
                .build()



    companion object {
        val BASE_URL: String = "https://jhone-364e5.firebaseio.com/"
        val BASE_STORAGE_URL: String = "gs://jhone-364e5.appspot.com"

        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(FirebaseUserIdTokenInterceptor())
                .build()
    }

}