package muoipt.githubuser.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import muoipt.githubuser.database.UserDb.Companion.DB_VERSION
import muoipt.githubuser.model.GithubUserEntity
import muoipt.githubuser.database.converters.ListStringConverter
import muoipt.githubuser.database.dao.UserDaoImpl

@Database(
    entities = [GithubUserEntity::class], version = DB_VERSION, exportSchema = false
)
@TypeConverters(ListStringConverter::class)
abstract class UserDb: RoomDatabase() {
    abstract fun userDao(): UserDaoImpl

    companion object {
        const val DB_VERSION = 1

        private const val DB_NAME = "GithubUserDb"

        private var instance: UserDb? = null
        fun getInstance(context: Context): UserDb {
            if (instance == null) {
                synchronized(UserDb::class) {
                    if (instance == null) {
                        instance = androidx.room.Room.databaseBuilder(
                            context, UserDb::class.java, DB_NAME
                        ).fallbackToDestructiveMigration().build()
                    }
                }
            }
            return instance!!
        }
    }
}