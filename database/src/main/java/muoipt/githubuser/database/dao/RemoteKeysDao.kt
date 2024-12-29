package muoipt.githubuser.database.dao

import androidx.room.Dao
import androidx.room.Query
import muoipt.githubuser.model.RemoteKeysEntity

interface RemoteKeysDao: BaseDao<RemoteKeysEntity> {
    suspend fun remoteKeysByUserLogin(login: String): RemoteKeysEntity?
    suspend fun clearRemoteKeys()
}

@Dao
abstract class RemoteKeysDaoImpl: RemoteKeysDao {

    @Query("SELECT * FROM remote_keys WHERE user_login = :login")
    abstract override suspend fun remoteKeysByUserLogin(login: String): RemoteKeysEntity?

    @Query("DELETE FROM remote_keys")
    abstract override suspend fun clearRemoteKeys()
}