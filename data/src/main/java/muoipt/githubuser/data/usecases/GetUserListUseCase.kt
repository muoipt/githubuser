package muoipt.githubuser.data.usecases

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import muoipt.githubuser.common.IoDispatcher
import muoipt.githubuser.data.common.AppLog
import muoipt.githubuser.data.mapper.toDataModel
import muoipt.githubuser.data.repositories.GithubUserRepo
import muoipt.githubuser.data.repositories.GithubUserRepo.Companion.USERS_PER_PAGE
import muoipt.githubuser.data.repositories.UsersRemoteMediator
import muoipt.githubuser.model.GithubUserData
import muoipt.githubuser.model.GithubUserEntity
import javax.inject.Inject

interface GetUserListUseCase {
    fun call(): Flow<PagingData<GithubUserData>>
}

class GetUserListUseCaseImpl @Inject constructor(
    private val githubUserRepo: GithubUserRepo,
    private val usersRemoteMediator: UsersRemoteMediator,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): GetUserListUseCase {

    @OptIn(ExperimentalPagingApi::class)
    override fun call(): Flow<PagingData<GithubUserData>> {
        AppLog.listing("Muoi123 => GetUserListUseCaseImpl call is triggered")

        return Pager(
            config = PagingConfig(pageSize = USERS_PER_PAGE, enablePlaceholders = false),
            remoteMediator = usersRemoteMediator,
        ) {
            githubUserRepo.loadAllUsersPaged()
        }.flow.flowOn(ioDispatcher)
            .map { value: PagingData<GithubUserEntity> ->
                value.map { entity ->
                    entity.toDataModel()
                }
            }
    }
}