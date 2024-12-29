package muoipt.githubuser.database.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import muoipt.githubuser.database.UserDb
import muoipt.githubuser.database.dao.RemoteKeysDao
import muoipt.githubuser.database.dao.UserDao

@Module
@InstallIn(SingletonComponent::class)
internal class DatabaseModule {
    @Provides
    fun database(@ApplicationContext context: Context): UserDb = UserDb.getInstance(context)

    @Provides
    fun userDao(db: UserDb): UserDao = db.userDao()

    @Provides
    fun remoteKeysDao(db: UserDb): RemoteKeysDao = db.remoteKeysDao()

}