package muoipt.githubuser.network

import kotlinx.coroutines.runBlocking
import muoipt.githubuser.model.GithubUser
import muoipt.githubuser.model.GithubUserDetail
import muoipt.githubuser.network.api.GithubUserApiService
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class GithubUserApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var githubUserApiService: GithubUserApiService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val client = OkHttpClient.Builder().build()
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        githubUserApiService = retrofit.create(GithubUserApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testGetUsers() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("[{\"login\":\"testuser\",\"id\":1,\"avatar_url\":\"https://example.com/avatar.png\"}]")
        mockWebServer.enqueue(mockResponse)

        val users: List<GithubUser> = githubUserApiService.getUsers(20, 100)
        assertEquals(1, users.size)
        assertEquals("testuser", users[0].login)
    }

    @Test
    fun testGetByUserLogin() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("{\"login\":\"testuser\",\"id\":1,\"avatar_url\":\"https://example.com/avatar.png\",\"location\":\"Test Location\",\"followers\":12,\"following\":1}")
        mockWebServer.enqueue(mockResponse)

        val userDetail: GithubUserDetail = githubUserApiService.getByUserLogin("testuser")
        assertEquals("testuser", userDetail.login)
        assertEquals("https://example.com/avatar.png", userDetail.avatarUrl)
        assertEquals("Test Location", userDetail.location)
        assertEquals(12, userDetail.followers)
        assertEquals(1, userDetail.following)
    }
}