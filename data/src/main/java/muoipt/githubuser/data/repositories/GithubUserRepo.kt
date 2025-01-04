package muoipt.githubuser.data.repositories

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import muoipt.githubuser.data.common.DataStrategy
import muoipt.githubuser.data.mapper.toDataDetailModel
import muoipt.githubuser.database.dao.UserDao
import muoipt.githubuser.model.GithubUserDetailData
import muoipt.githubuser.model.GithubUserEntity
import muoipt.githubuser.network.api.GitHubUserApi
import javax.inject.Inject

interface GithubUserRepo {
    fun getUserDetail(
        strategy: DataStrategy = DataStrategy.LOCAL,
        loginUserName: String
    ): Flow<GithubUserDetailData?>

    fun loadAllUsersPaged(): PagingSource<Int, GithubUserEntity>
}

class GithubUserRepoImpl @Inject constructor(
    private val userRemoteApi: GitHubUserApi,
    private val userLocalApi: UserDao,
): GithubUserRepo {

    override fun loadAllUsersPaged(): PagingSource<Int, GithubUserEntity> {
        val result = userLocalApi.loadAllUserPaged()
        return result
    }

    override fun getUserDetail(
        strategy: DataStrategy,
        loginUserName: String
    ): Flow<GithubUserDetailData?> {
        val willFetchFromRemote = when (strategy) {
            DataStrategy.REMOTE -> true
            else -> false
        }
        return userLocalApi.getByUserLogin(loginUserName)
            .onStart {
                if (willFetchFromRemote) fetchUserByUserName(loginUserName)
            }
            .onEach {
                if (it?.isFetchedWithDetail() == false) {
                    fetchUserByUserName(loginUserName)
                }
            }
            .map { it?.toDataDetailModel() }
    }

    private suspend fun fetchUserByUserName(loginUserName: String) {
        val response = userRemoteApi.getByUserLogin(loginUserName)
        userLocalApi.updateUser(
            response.login,
            response.location ?: "",
            response.followers ?: 0,
            response.following ?: 0
        )
    }
}