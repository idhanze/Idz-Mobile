package com.example.idzmobile.di

import com.example.idzmobile.data.ApiInteface
import com.example.idzmobile.data.DataRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Singleton
    @Provides
    fun provideInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    @Singleton
    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor) : OkHttpClient =
        OkHttpClient()
            .newBuilder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    
    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()
    
    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient) : Retrofit = Retrofit.Builder()
        .baseUrl("https://green-thumb-64168.uc.r.appspot.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    
    @Singleton
    @Provides
    fun provideApiInterface(retrofit: Retrofit) : ApiInteface = retrofit.create(ApiInteface::class.java)
    
    @Singleton
    @Provides
    fun provideRepository(apiInteface: ApiInteface) = DataRepository(apiInteface)
    
    
}