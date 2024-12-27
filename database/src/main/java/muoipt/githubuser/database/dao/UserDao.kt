package muoipt.githubuser.database.dao

import androidx.room.Dao
import kotlinx.coroutines.flow.Flow
import muoipt.githubuser.model.GithubUserEntity

interface UserDao: BaseDao<GithubUserEntity> {
    fun getAll(): Flow<List<GithubUserEntity>>

    fun getByUserLogin(login: String): Flow<GithubUserEntity?>

    suspend fun deleteAll()
}

@Dao
abstract class UserDaoImpl: UserDao {
    @androidx.room.Query("SELECT * FROM github_user")
    abstract override fun getAll(): Flow<List<GithubUserEntity>>

    @androidx.room.Query("SELECT * FROM github_user WHERE login = :login")
    abstract override fun getByUserLogin(login: String): Flow<GithubUserEntity?>

    @androidx.room.Query("DELETE FROM github_user")
    abstract override suspend fun deleteAll()
}