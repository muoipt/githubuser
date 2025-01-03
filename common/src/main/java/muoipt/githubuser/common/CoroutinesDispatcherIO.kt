package muoipt.githubuser.common
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import muoipt.githubuser.common.IoDispatcher

@InstallIn(SingletonComponent::class)
@Module
object CoroutinesDispatchersModule {
    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}