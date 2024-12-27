package muoipt.githubuser.data.usecases

import kotlinx.coroutines.flow.Flow
import muoipt.githubuser.data.common.DataStrategy
import muoipt.githubuser.data.repositories.GithubUserRepo
import muoipt.githubuser.model.GithubUserData
import javax.inject.Inject

interface GetUserListUseCase {
    suspend fun getUser(withRefresh: Boolean): Flow<List<GithubUserData>>
}

class GetUserListUseCaseImpl @Inject constructor(
    private val githubUserRepo: GithubUserRepo
): GetUserListUseCase {
    override suspend fun getUser(withRefresh: Boolean): Flow<List<GithubUserData>> {
        val dataStrategy = if(withRefresh){
            DataStrategy.REMOTE
        } else {
            DataStrategy.LOCAL
        }
        return githubUserRepo.getUsers(dataStrategy, 100)
    }
}