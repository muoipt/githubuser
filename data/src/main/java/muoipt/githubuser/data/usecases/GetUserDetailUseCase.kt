package muoipt.githubuser.data.usecases

import kotlinx.coroutines.flow.Flow
import muoipt.githubuser.data.repositories.GithubUserRepo
import muoipt.githubuser.model.GithubUserDetailData
import javax.inject.Inject

interface GetUserDetailUseCase {
    suspend fun getUserDetail(login: String): Flow<GithubUserDetailData?>
}

class GetUserDetailUseCaseImpl @Inject constructor(
    private val githubUserRepo: GithubUserRepo,
): GetUserDetailUseCase {
    override suspend fun getUserDetail(login: String): Flow<GithubUserDetailData?> {
        return githubUserRepo.getUserDetail(loginUserName = login)
    }
}