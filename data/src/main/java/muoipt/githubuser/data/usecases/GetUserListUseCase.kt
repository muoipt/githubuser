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
    //    suspend fun getUser(withRefresh: Boolean): Flow<List<GithubUserData>>
    fun getUser(): Flow<PagingData<GithubUserData>>
    fun call(): Flow<PagingData<GithubUserData>>
}

class GetUserListUseCaseImpl @Inject constructor(
    private val githubUserRepo: GithubUserRepo,
    private val usersRemoteMediator: UsersRemoteMediator,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): GetUserListUseCase {
//    override suspend fun getUser(withRefresh: Boolean): Flow<List<GithubUserData>> {
//        val dataStrategy = if(withRefresh){
//            DataStrategy.REMOTE
//        } else {
//            DataStrategy.LOCAL
//        }
//        return githubUserRepo.getUsers(dataStrategy, 100)
//    }

    override fun getUser(): Flow<PagingData<GithubUserData>> {
        AppLog.listing("GetUserListUseCaseImpl getUser")

        return githubUserRepo.getUserWithPaging()
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun call(): Flow<PagingData<GithubUserData>> {
        AppLog.listing("Muoi123 => GetUserListUseCaseImpl call is triggered")

        return Pager(
            config = PagingConfig(USERS_PER_PAGE),
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