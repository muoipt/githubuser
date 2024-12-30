package muoipt.githubuser.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import muoipt.githubuser.data.mapper.toEntity
import muoipt.githubuser.data.usecases.GetUserListUseCaseImpl.Companion.USERS_PER_PAGE
import muoipt.githubuser.database.UserDb
import muoipt.githubuser.database.dao.UserDao
import muoipt.githubuser.model.GithubUserEntity
import muoipt.githubuser.model.RemoteKeysEntity
import muoipt.githubuser.network.api.GitHubUserApi
import java.io.IOException
import javax.inject.Inject

private const val GITHUB_USER_STARTING_INDEX = 0

@OptIn(ExperimentalPagingApi::class)
class UsersRemoteMediator @Inject constructor(
    private val db: UserDb,
    private val githubUserDao: UserDao,
    private val remoteDataSource: GitHubUserApi
) : RemoteMediator<Int, GithubUserEntity>() {

    override suspend fun initialize(): InitializeAction {
        // Launch remote refresh for the first time launch when local database does not have any item
        // and skip init refresh if local db already store data
        val existingItemsInDb = githubUserDao.count()

        return if(existingItemsInDb == 0) InitializeAction.LAUNCH_INITIAL_REFRESH
        else InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, GithubUserEntity>): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(USERS_PER_PAGE) ?: GITHUB_USER_STARTING_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for prepend.
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its nextKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val response = remoteDataSource.getUsers(USERS_PER_PAGE, page)
            val users = response.map { it.toEntity() }

            val endOfPaginationReached = users.isEmpty()
            db.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    db.remoteKeysDao().clearRemoteKeys()
                    db.userDao().deleteAll()
                }
                val prevKey = if (page == GITHUB_USER_STARTING_INDEX) null else page - USERS_PER_PAGE
                val nextKey = if (endOfPaginationReached) null else page + USERS_PER_PAGE
                val keys = users.map {
                    RemoteKeysEntity(userLogin = it.login, prevKey = prevKey, nextKey = nextKey)
                }
                db.remoteKeysDao().insertAll(keys)
                db.userDao().insertAll(users)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, GithubUserEntity>): RemoteKeysEntity? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { repo ->
                // Get the remote keys of the last item retrieved
                db.remoteKeysDao().remoteKeysByUserLogin(repo.login)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, GithubUserEntity>): RemoteKeysEntity? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                // Get the remote keys of the first items retrieved
                db.remoteKeysDao().remoteKeysByUserLogin(repo.login)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, GithubUserEntity>
    ): RemoteKeysEntity? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.login?.let { userLogin ->
                db.remoteKeysDao().remoteKeysByUserLogin(userLogin)
            }
        }
    }
}
