package muoipt.githubuser.data.repositories

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import muoipt.githubuser.data.common.AppLog
import muoipt.githubuser.data.common.DataStrategy
import muoipt.githubuser.data.mapper.toDataDetailModel
import muoipt.githubuser.data.mapper.toEntity
import muoipt.githubuser.database.dao.UserDao
import muoipt.githubuser.model.GithubUserDetailData
import muoipt.githubuser.model.GithubUserEntity
import muoipt.githubuser.network.api.GitHubUserApi
import javax.inject.Inject

interface GithubUserRepo {
    fun getUserDetail(
        strategy: DataStrategy = DataStrategy.AUTO,
        loginUserName: String
    ): Flow<GithubUserDetailData?>

    fun loadAllUsersPaged(): PagingSource<Int, GithubUserEntity>

    companion object {
        const val USERS_PER_PAGE = 20
    }
}

class GithubUserRepoImpl @Inject constructor(
    private val userRemoteApi: GitHubUserApi,
    private val userLocalApi: UserDao,
): GithubUserRepo {

    override fun loadAllUsersPaged(): PagingSource<Int, GithubUserEntity> {
        val result = userLocalApi.loadAllUserPaged()
        AppLog.listing("Muoi123 -> GithubUserRepoImpl loadAllUsersPaged result = $result")
        return result
    }

    override fun getUserDetail(
        strategy: DataStrategy,
        loginUserName: String
    ): Flow<GithubUserDetailData?> {
        val willFetchFromRemote = when (strategy) {
            DataStrategy.REMOTE,
            DataStrategy.AUTO -> true

            else -> false
        }
        return userLocalApi.getByUserLogin(loginUserName)
            .onStart {
                if (willFetchFromRemote) fetchUserByUserName(loginUserName)
            }
            .map { it?.toDataDetailModel() }
    }

    private suspend fun fetchUserByUserName(loginUserName: String) {
        val response = userRemoteApi.getUserByUserName(loginUserName)
        userLocalApi.update(response.toEntity())
    }
}