package muoipt.githubuser.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import muoipt.githubuser.model.GithubUserEntity

interface UserDao: BaseDao<GithubUserEntity> {

    fun getByUserLogin(login: String): Flow<GithubUserEntity?>

    suspend fun count(): Int

    suspend fun deleteAll()

    fun loadAllUserPaged(): PagingSource<Int, GithubUserEntity>

    suspend fun updateUser(login: String, location: String, followers: Int, following: Int)
}

@Dao
abstract class UserDaoImpl: UserDao {

    @Query("SELECT * FROM github_user WHERE login = :login")
    abstract override fun getByUserLogin(login: String): Flow<GithubUserEntity?>

    @Query("SELECT COUNT(*) FROM github_user")
    abstract override suspend fun count(): Int

    @Query("DELETE FROM github_user")
    abstract override suspend fun deleteAll()

    @Query("SELECT * FROM github_user")
    abstract override fun loadAllUserPaged(): PagingSource<Int, GithubUserEntity>

    @Query("UPDATE github_user SET location = :location, followers = :followers, following = :following WHERE login = :login")
    abstract override suspend fun updateUser(
        login: String,
        location: String,
        followers: Int,
        following: Int
    )
}

