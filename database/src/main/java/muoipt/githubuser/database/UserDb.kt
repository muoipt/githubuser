package muoipt.githubuser.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import muoipt.githubuser.database.UserDb.Companion.DB_VERSION
import muoipt.githubuser.database.converters.ListStringConverter
import muoipt.githubuser.database.dao.RemoteKeysDaoImpl
import muoipt.githubuser.database.dao.UserDaoImpl
import muoipt.githubuser.model.GithubUserEntity
import muoipt.githubuser.model.RemoteKeysEntity

@Database(
    entities = [GithubUserEntity::class, RemoteKeysEntity::class],
    version = DB_VERSION,
    exportSchema = false
)
@TypeConverters(ListStringConverter::class)
abstract class UserDb: RoomDatabase() {
    abstract fun userDao(): UserDaoImpl
    abstract fun remoteKeysDao(): RemoteKeysDaoImpl

    companion object {

        const val DB_VERSION = 1

        private const val DB_NAME = "GithubUserDb"

        @Volatile
        private var INSTANCE: UserDb? = null

        fun getInstance(context: Context): UserDb =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                UserDb::class.java, DB_NAME
            )
                .build()
    }
}