package muoipt.githubuser.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import muoipt.githubuser.data.repositories.GithubUserRepo
import muoipt.githubuser.data.repositories.GithubUserRepoImpl
import muoipt.githubuser.data.usecases.GetUserDetailUseCase
import muoipt.githubuser.data.usecases.GetUserDetailUseCaseImpl
import muoipt.githubuser.data.usecases.GetUserListUseCase
import muoipt.githubuser.data.usecases.GetUserListUseCaseImpl


@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindArticleRepo(articleRepoImpl: GithubUserRepoImpl): GithubUserRepo

    @Binds
    fun bindGetArticleUseCase(getArticleUseCaseImpl: GetUserListUseCaseImpl): GetUserListUseCase

    @Binds
    fun bindGetArticleDetailUseCase(getArticleDetailUseCaseImpl: GetUserDetailUseCaseImpl): GetUserDetailUseCase

}