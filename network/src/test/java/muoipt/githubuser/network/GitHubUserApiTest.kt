package muoipt.githubuser.network

import kotlinx.coroutines.runBlocking
import muoipt.githubuser.model.GithubUser
import muoipt.githubuser.model.GithubUserDetail
import muoipt.githubuser.network.api.GitHubUserApi
import muoipt.githubuser.network.api.GithubUserApiService
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GitHubUserApiTest {

    @Mock
    private lateinit var githubUserApiService: GithubUserApiService

    private lateinit var gitHubUserApi: GitHubUserApi

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        gitHubUserApi = GitHubUserApi(githubUserApiService)
    }

    @Test
    fun testGetUsers() = runBlocking {
        val mockUsers = listOf(
            GithubUser(login = "user1"),
            GithubUser(login = "user2")
        )
        `when`(githubUserApiService.getUsers(20, 100)).thenReturn(mockUsers)

        val users = gitHubUserApi.getUsers(20, 100)
        assertEquals(mockUsers, users)
    }

    @Test
    fun testGetByUserLogin() = runBlocking {
        val mockUserDetail = GithubUserDetail(login = "user1", location = "VN")
        `when`(githubUserApiService.getByUserLogin("user1")).thenReturn(mockUserDetail)

        val userDetail = gitHubUserApi.getByUserLogin("user1")
        assertEquals(mockUserDetail, userDetail)
    }
}