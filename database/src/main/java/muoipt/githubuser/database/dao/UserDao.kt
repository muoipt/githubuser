package muoipt.githubuser.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import muoipt.githubuser.model.GithubUserEntity

interface UserDao: BaseDao<GithubUserEntity> {
    fun getAll(): PagingSource<Int, GithubUserEntity>

    fun getAllUsers(): List<GithubUserEntity>

    fun getByUserLogin(login: String): Flow<GithubUserEntity?>

    fun count(): Int

    suspend fun deleteAll()

    suspend fun getUsersByRange(offset: Int, limit: Int): List<GithubUserEntity>
}

@Dao
abstract class UserDaoImpl: UserDao {
    @Query("SELECT * FROM github_user")
    abstract override fun getAll(): PagingSource<Int, GithubUserEntity>

    @Query("SELECT * FROM github_user")
    abstract override fun getAllUsers(): List<GithubUserEntity>

    @Query("SELECT * FROM github_user WHERE login = :login")
    abstract override fun getByUserLogin(login: String): Flow<GithubUserEntity?>

    @Query("SELECT COUNT(*) FROM github_user")
    abstract override fun count(): Int

    @Query("DELETE FROM github_user")
    abstract override suspend fun deleteAll()

    @Query("SELECT * FROM github_user LIMIT :limit OFFSET :offset")
    abstract override suspend fun getUsersByRange(offset: Int, limit: Int): List<GithubUserEntity>
}