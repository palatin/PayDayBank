package com.payday.paydaybank.di.modules

import com.payday.paydaybank.BuildConfig
import com.payday.paydaybank.api.AuthApi
import com.payday.paydaybank.api.TransactionApi
import com.payday.paydaybank.util.StringToBigDecimal
import com.payday.paydaybank.util.StringToBigDecimalAdapter
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @Named("base_url")
    fun provideBaseUrl(): String = "http://192.168.0.107:3000"

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient = OkHttpClient.Builder().apply {
        if(BuildConfig.DEBUG)
            addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BASIC) })
    }.build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, @Named("base_url") url: String, moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @Singleton
    fun provideTransactionsApi(retrofit: Retrofit): TransactionApi = retrofit.create(TransactionApi::class.java)

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().add(StringToBigDecimalAdapter()).build()

}