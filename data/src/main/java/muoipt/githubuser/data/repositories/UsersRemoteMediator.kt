package muoipt.githubuser.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import muoipt.githubuser.data.common.AppLog
import muoipt.githubuser.data.mapper.toEntity
import muoipt.githubuser.database.UserDb
import muoipt.githubuser.database.dao.UserDao
import muoipt.githubuser.model.GithubUserEntity
import muoipt.githubuser.network.api.GitHubUserApi
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class UsersRemoteMediator @Inject constructor(
    private val db: UserDb,
    private val githubUserDao: UserDao,
    private val remoteDataSource: GitHubUserApi
): RemoteMediator<Int, GithubUserEntity>() {

    companion object {
        var pageCount = 0 // number of pages are currently loaded
    }

    override suspend fun initialize(): InitializeAction {
        val existingItemsInDb = githubUserDao.count()
        pageCount = existingItemsInDb / 20

        return if(existingItemsInDb == 0) InitializeAction.LAUNCH_INITIAL_REFRESH
        else InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, GithubUserEntity>
    ): MediatorResult {
        AppLog.listing("Muoi*** -> UsersRemoteMediator load loadType = $loadType")

        return try {
            if (loadType == LoadType.PREPEND) return MediatorResult.Success(endOfPaginationReached = true)

            val since = pageCount * state.config.pageSize
            pageCount++
            AppLog.listing("Muoi*** -> UsersRemoteMediator load remoteDataSource.getUsers with itemPerPage = ${state.config.pageSize} and since = $since")

            // Check if data already exists in the local database
            val existingUsers = githubUserDao.getUsersByRange(since, state.config.pageSize)
            if (existingUsers.isNotEmpty() && existingUsers.size == state.config.pageSize) {
                AppLog.listing("Muoi*** -> Data already exists in the local database, skipping network request")
                return MediatorResult.Success(endOfPaginationReached = false)
            }

            val response = remoteDataSource.getUsers(state.config.pageSize, since)
            val users = response.map { it.toEntity() }

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    AppLog.listing("UsersRemoteMediator load loadType == LoadType.REFRESH")
                    githubUserDao.deleteAll()
                }
                val insertResult = githubUserDao.insertAll(users)
                AppLog.listing("Muoi*** -> insertResult for users (${users.size}) = $insertResult")
            }

            MediatorResult.Success(endOfPaginationReached = users.isEmpty())
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}