package muoipt.githubuser.data.usecases

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import muoipt.githubuser.common.IoDispatcher
import muoipt.githubuser.data.repositories.GithubUserRepo
import muoipt.githubuser.model.GithubUserDetailData
import javax.inject.Inject

interface GetUserDetailUseCase {
    suspend fun getUserDetail(login: String): Flow<GithubUserDetailData?>
}

class GetUserDetailUseCaseImpl @Inject constructor(
    private val githubUserRepo: GithubUserRepo,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): GetUserDetailUseCase {
    override suspend fun getUserDetail(login: String): Flow<GithubUserDetailData?> {
        return githubUserRepo.getUserDetail(loginUserName = login).flowOn(ioDispatcher)
    }
}