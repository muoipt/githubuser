package muoipt.githubuser.data.usecases

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import muoipt.githubuser.data.common.AppLog
import muoipt.githubuser.data.repositories.GithubUserRepo
import muoipt.githubuser.model.GithubUserData
import javax.inject.Inject

interface GetUserListUseCase {
//    suspend fun getUser(withRefresh: Boolean): Flow<List<GithubUserData>>
     fun getUser(): Flow<PagingData<GithubUserData>>
}

class GetUserListUseCaseImpl @Inject constructor(
    private val githubUserRepo: GithubUserRepo
): GetUserListUseCase {
//    override suspend fun getUser(withRefresh: Boolean): Flow<List<GithubUserData>> {
//        val dataStrategy = if(withRefresh){
//            DataStrategy.REMOTE
//        } else {
//            DataStrategy.LOCAL
//        }
//        return githubUserRepo.getUsers(dataStrategy, 100)
//    }

    override  fun getUser(): Flow<PagingData<GithubUserData>> {
        AppLog.listing("GetUserListUseCaseImpl getUser")

        return githubUserRepo.getUserWithPaging()
    }
}