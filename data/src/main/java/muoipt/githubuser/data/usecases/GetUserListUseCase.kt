package muoipt.githubuser.data.usecases


import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import muoipt.githubuser.data.mapper.toDataModel
import muoipt.githubuser.data.repositories.GithubUserRepo
import muoipt.githubuser.data.repositories.UsersRemoteMediator
import muoipt.githubuser.model.GithubUserData
import muoipt.githubuser.model.GithubUserEntity
import javax.inject.Inject

interface GetUserListUseCase {
    fun execute(): Flow<PagingData<GithubUserData>>
}

class GetUserListUseCaseImpl @Inject constructor(
    private val usersRemoteMediator: UsersRemoteMediator,
    private val githubUserRepo: GithubUserRepo
): GetUserListUseCase {

    companion object {
        const val USERS_PER_PAGE = 20
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun execute(): Flow<PagingData<GithubUserData>> {
        return Pager(
            config = PagingConfig(
                pageSize = USERS_PER_PAGE,
                enablePlaceholders = false
            ),
            remoteMediator = usersRemoteMediator,
        ) {
            githubUserRepo.loadAllUsersPaged()
        }.flow.map { value: PagingData<GithubUserEntity> ->
            value.map { entity ->
                entity.toDataModel()
            }
        }
    }
}