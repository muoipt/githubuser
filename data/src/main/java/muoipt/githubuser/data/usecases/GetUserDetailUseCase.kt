package muoipt.githubuser.data.usecases

import kotlinx.coroutines.flow.Flow
import muoipt.githubuser.model.GithubUserDetailData
import muoipt.githubuser.data.repositories.GithubUserRepo
import javax.inject.Inject

interface GetUserDetailUseCase {
    suspend fun getUserDetail(title: String): Flow<GithubUserDetailData?>
}

class GetUserDetailUseCaseImpl @Inject constructor(
    private val githubUserRepo: GithubUserRepo
): GetUserDetailUseCase {
    override suspend fun getUserDetail(title: String): Flow<GithubUserDetailData?> {
        return githubUserRepo.getUserDetail(title)
    }
}