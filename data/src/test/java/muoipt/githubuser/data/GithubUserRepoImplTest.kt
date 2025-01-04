package muoipt.githubuser.data

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import muoipt.githubuser.data.common.DataStrategy
import muoipt.githubuser.data.mapper.toDataDetailModel
import muoipt.githubuser.data.repositories.GithubUserRepoImpl
import muoipt.githubuser.database.dao.UserDao
import muoipt.githubuser.model.GithubUserDetail
import muoipt.githubuser.model.GithubUserEntity
import muoipt.githubuser.network.api.GitHubUserApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.times

class GithubUserRepoImplTest {

    @Mock
    private lateinit var userRemoteApi: GitHubUserApi

    @Mock
    private lateinit var userLocalApi: UserDao

    private lateinit var githubUserRepo: GithubUserRepoImpl

    private val testLoginUserName = "testLoginUser"

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        githubUserRepo = GithubUserRepoImpl(userRemoteApi, userLocalApi)
    }

    @Test
    fun `loadAllUsersPaged return PagingSource from userLocalApi`() {
        val mockPagingSource =
            Mockito.mock(PagingSource::class.java) as PagingSource<Int, GithubUserEntity>
        `when`(userLocalApi.loadAllUserPaged()).thenReturn(mockPagingSource)

        val result = githubUserRepo.loadAllUsersPaged()
        assertNotNull(result)
        assertEquals(mockPagingSource, result)
    }

    @Test
    fun `get user by username from local db when detail is available`() = runBlocking {
        val mockUserEntity = GithubUserEntity(login = testLoginUserName, location = "VN")
        `when`(userLocalApi.getByUserLogin(testLoginUserName)).thenReturn(flowOf(mockUserEntity))

        val result = githubUserRepo.getUserDetail(DataStrategy.LOCAL, testLoginUserName).first()
        assertNotNull(result)
        assertEquals(mockUserEntity.toDataDetailModel(), result)
        Mockito.verify(userRemoteApi, times(0)).getByUserLogin(any())
        Mockito.verify(userLocalApi, times(0)).updateUser(any(), any(), any(), any())

    }

    @Test
    fun `fetch user by username when detail is not available in db`() = runBlocking {
        val mockUserEntity = GithubUserEntity(login = testLoginUserName)
        val mockUserDetail = GithubUserDetail(
            login = testLoginUserName,
            location = "testLocation",
            avatarUrl = "",
            htmlUrl = "",
            followers = 100,
            following = 200
        )
        val mockUserDetailData = mockUserEntity.toDataDetailModel()
        `when`(userLocalApi.getByUserLogin(testLoginUserName)).thenReturn(flowOf(mockUserEntity))
        `when`(userRemoteApi.getByUserLogin(testLoginUserName)).thenReturn(mockUserDetail)

        val result = githubUserRepo.getUserDetail(DataStrategy.LOCAL, testLoginUserName).first()
        assertNotNull(result)
        assertEquals(mockUserDetailData, result)
        Mockito.verify(userRemoteApi, times(1)).getByUserLogin(testLoginUserName)
        Mockito.verify(userLocalApi).updateUser(testLoginUserName, "testLocation", 100, 200)
    }

    @Test
    fun `fetch user by username when input DataStrategy REMOTE`() = runBlocking {
        val mockUserEntity = GithubUserEntity(login = testLoginUserName, location = "VN")
        val mockUserDetail = GithubUserDetail(
            login = testLoginUserName,
            location = "testLocation",
            avatarUrl = "",
            htmlUrl = "",
            followers = 100,
            following = 200
        )
        val mockUserDetailData = mockUserEntity.toDataDetailModel()
        `when`(userLocalApi.getByUserLogin(testLoginUserName)).thenReturn(flowOf(mockUserEntity))
        `when`(userRemoteApi.getByUserLogin(testLoginUserName)).thenReturn(mockUserDetail)

        val result = githubUserRepo.getUserDetail(DataStrategy.REMOTE, testLoginUserName).first()
        assertNotNull(result)
        assertEquals(mockUserDetailData, result)
        Mockito.verify(userRemoteApi, times(1)).getByUserLogin(testLoginUserName)
        Mockito.verify(userLocalApi).updateUser(testLoginUserName, "testLocation", 100, 200)
    }
}
