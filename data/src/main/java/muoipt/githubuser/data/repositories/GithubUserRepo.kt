package muoipt.githubuser.data.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import muoipt.githubuser.common.IoDispatcher
import muoipt.githubuser.data.common.AppLog
import muoipt.githubuser.data.common.DataStrategy
import muoipt.githubuser.data.mapper.toDataDetailModel
import muoipt.githubuser.data.mapper.toDataModel
import muoipt.githubuser.data.mapper.toEntity
import muoipt.githubuser.data.repositories.GithubUserRepo.Companion.USERS_PER_PAGE
import muoipt.githubuser.database.dao.UserDao
import muoipt.githubuser.database.dao.upsert
import muoipt.githubuser.model.GithubUserData
import muoipt.githubuser.model.GithubUserDetailData
import muoipt.githubuser.network.api.GitHubUserApi
import javax.inject.Inject

interface GithubUserRepo {
    fun getUsers(strategy: DataStrategy = DataStrategy.AUTO, since: Int): Flow<List<GithubUserData>>
    fun getUserDetail(loginUserName: String): Flow<GithubUserDetailData?>

    companion object {
        const val USERS_PER_PAGE = 20
    }
}

class GithubUserRepoImpl @Inject constructor(
    private val userRemoteApi: GitHubUserApi,
    private val userLocalApi: UserDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): GithubUserRepo {
    override fun getUsers(strategy: DataStrategy, since: Int): Flow<List<GithubUserData>> {
        val willFetchFromRemote = when (strategy) {
            DataStrategy.REMOTE,
            DataStrategy.AUTO -> true

            else -> false
        }

        return userLocalApi.getAll()
            .onStart { if (willFetchFromRemote) fetchUsers(since) }
            .map { response -> response.map { it.toDataModel() } }
            .flowOn(ioDispatcher)
    }

    private suspend fun fetchUsers(since: Int) {
        val remoteResponse = userRemoteApi.getUsers(USERS_PER_PAGE, since)
        val userEntities = remoteResponse.results.map { it.toEntity() }

        AppLog.listing("fetchUsers userRemoteApi.getUsers with since = $since")
        userLocalApi.upsert(userEntities)
    }

    override fun getUserDetail(loginUserName: String): Flow<GithubUserDetailData?> {
        return userLocalApi.getByUserLogin(loginUserName).map { it?.toDataDetailModel() }
    }
}