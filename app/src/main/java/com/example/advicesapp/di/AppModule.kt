package com.example.advicesapp.di

import android.content.Context
import androidx.room.Room
import com.example.advicesapp.common.Constants
import com.example.advicesapp.data.local.AdviceDao
import com.example.advicesapp.data.local.AdviceDatabase
import com.example.advicesapp.data.remote.AdviceApi
import com.example.advicesapp.data.repository.AdviceRepositoryImpl
import com.example.advicesapp.domain.repository.AdviceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, AdviceDatabase::class.java, "AdviceDB").build()


    @Singleton
    @Provides
    fun provideDao(
        database: AdviceDatabase
    ) = database.adviceDao()

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun provideConvertorFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideOkhttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
        return okHttpClient.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        converterFactory: Converter.Factory,
        okHttpClient: OkHttpClient
    ): Retrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
        return retrofit.build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): AdviceApi {
        return retrofit.create(AdviceApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAdviceRepository(api: AdviceApi, adviceDao: AdviceDao): AdviceRepository {
        return AdviceRepositoryImpl(api, adviceDao)
    }
}