package com.tlnacl.githubcompose.di

import com.tlnacl.githubcompose.BuildConfig
import com.tlnacl.githubcompose.data.GithubApi
import com.tlnacl.githubcompose.data.GithubRepository
import com.tlnacl.githubcompose.data.GithubRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    @Singleton
    @Provides
    fun provideGithubApi(): GithubApi {
        val httpClientBuilder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor { message ->
                Timber.tag("OkHttp").d(message)
            }
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
            httpClientBuilder.addInterceptor(loggingInterceptor)
        }
        val okHttpClient = httpClientBuilder.build()

        val restAdapter = Retrofit.Builder()
            .baseUrl(BuildConfig.API_ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        return restAdapter.create(GithubApi::class.java)
    }

    @Singleton
    @Provides
    fun provideGithubRepository(repository: GithubRepositoryImpl): GithubRepository = repository
}