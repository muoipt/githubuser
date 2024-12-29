package muoipt.githubuser.data.repositories

import androidx.paging.PagingSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import muoipt.githubuser.common.IoDispatcher
import muoipt.githubuser.data.common.AppLog
import muoipt.githubuser.data.mapper.toDataDetailModel
import muoipt.githubuser.data.mapper.toEntity
import muoipt.githubuser.data.repositories.GithubUserRepo.Companion.USERS_PER_PAGE
import muoipt.githubuser.database.dao.UserDao
import muoipt.githubuser.database.dao.upsert
import muoipt.githubuser.model.GithubUserDetailData
import muoipt.githubuser.model.GithubUserEntity
import muoipt.githubuser.network.api.GitHubUserApi
import javax.inject.Inject

interface GithubUserRepo {
//    fun getUsers(strategy: DataStrategy = DataStrategy.AUTO, since: Int): Flow<List<GithubUserData>>
    fun getUserDetail(loginUserName: String): Flow<GithubUserDetailData?>
//    fun getUserWithPaging():Flow<PagingData<GithubUserData>>

    fun loadAllUsersPaged():PagingSource<Int, GithubUserEntity>

    companion object {
        const val USERS_PER_PAGE = 20
    }
}

class GithubUserRepoImpl @Inject constructor(
    private val userRemoteApi: GitHubUserApi,
    private val userLocalApi: UserDao,
//    private val usersRemoteMediator: UsersRemoteMediator,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): GithubUserRepo {
//    override fun getUsers(strategy: DataStrategy, since: Int): Flow<List<GithubUserData>> {
//        val willFetchFromRemote = when (strategy) {
//            DataStrategy.REMOTE,
//            DataStrategy.AUTO -> true
//
//            else -> false
//        }
//
//        return userLocalApi.getAll()
//            .onStart { if (willFetchFromRemote) fetchUsers(since) }
//            .map { response -> response.map { it.toDataModel() } }
//            .flowOn(ioDispatcher)
//    }

    private suspend fun fetchUsers(since: Int) {
        val remoteResponse = userRemoteApi.getUsers(USERS_PER_PAGE, since)
        val userEntities = remoteResponse.map { it.toEntity() }

        AppLog.listing("fetchUsers userRemoteApi.getUsers with since = $since")
        userLocalApi.upsert(userEntities)
    }

    override fun getUserDetail(loginUserName: String): Flow<GithubUserDetailData?> {
        return userLocalApi.getByUserLogin(loginUserName).map { it?.toDataDetailModel() }
    }

//    @OptIn(ExperimentalPagingApi::class)
//    override fun getUserWithPaging():Flow<PagingData<GithubUserData>>{
//        AppLog.listing("GithubUserRepoImpl getUserWithPaging")
//
//        return Pager(
//            config = PagingConfig(
//                pageSize = USERS_PER_PAGE,
//            ),
//            remoteMediator = usersRemoteMediator,
//            pagingSourceFactory = {
//                AppLog.listing("GithubUserRepoImpl getUserWithPaging userLocalApi.getAll()")
//
//                userLocalApi.getAll()
//            }
//        ).flow.flowOn(ioDispatcher).map { it.map { entity -> entity.toDataModel() } }
//    }

    override fun loadAllUsersPaged():PagingSource<Int, GithubUserEntity>{
        val result = userLocalApi.loadAllUserPaged()
        AppLog.listing("Muoi123 -> GithubUserRepoImpl loadAllUsersPaged result = $result")
        return result
    }
}