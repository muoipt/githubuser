package muoipt.githubuser.network.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import muoipt.githubuser.network.api.GitHubUserApi
import muoipt.githubuser.network.api.GitHubUserApiInterface

@Module
@InstallIn(SingletonComponent::class)
interface ApiModule {
    @Binds
    fun bindGitHubUserApi(impl: GitHubUserApi): GitHubUserApiInterface
}