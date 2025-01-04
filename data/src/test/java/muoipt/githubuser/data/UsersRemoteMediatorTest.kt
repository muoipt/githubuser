
import androidx.paging.ExperimentalPagingApi
import androidx.paging.RemoteMediator
import kotlinx.coroutines.test.runTest
import muoipt.githubuser.data.repositories.UsersRemoteMediator
import muoipt.githubuser.database.UserDb
import muoipt.githubuser.database.dao.RemoteKeysDaoImpl
import muoipt.githubuser.database.dao.UserDaoImpl
import muoipt.githubuser.network.api.GitHubUserApi
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class UsersRemoteMediatorTest {

    @Mock
    private lateinit var mockDb: UserDb

    @Mock
    private lateinit var mockUserDao: UserDaoImpl

    @Mock
    private lateinit var mockRemoteKeysDao: RemoteKeysDaoImpl

    @Mock
    private lateinit var mockApi: GitHubUserApi

    private lateinit var remoteMediator: UsersRemoteMediator

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        `when`(mockDb.userDao()).thenReturn(mockUserDao)
        `when`(mockDb.remoteKeysDao()).thenReturn(mockRemoteKeysDao)
        remoteMediator = UsersRemoteMediator(mockDb, mockUserDao, mockApi)
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun `initialize should return LAUNCH_INITIAL_REFRESH when database is empty`() = runTest {
        `when`(mockUserDao.count()).thenReturn(0)
        val result = remoteMediator.initialize()
        assert(result == RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH)
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun `initialize should return SKIP_INITIAL_REFRESH when database is not empty`() = runTest {
        `when`(mockUserDao.count()).thenReturn(1)
        val result = remoteMediator.initialize()
        assert(result == RemoteMediator.InitializeAction.SKIP_INITIAL_REFRESH)
    }
}