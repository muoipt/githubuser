package muoipt.githubuser.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import muoipt.githubuser.network.api.GithubUserApiService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiServiceModule {
    @Provides
    @Singleton
    fun provideGithubUserApiService(retrofit: Retrofit): GithubUserApiService {
        return retrofit.create(GithubUserApiService::class.java)
    }
}