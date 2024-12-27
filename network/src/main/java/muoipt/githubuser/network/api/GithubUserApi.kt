package muoipt.githubuser.network.api

import muoipt.githubuser.model.GithubUser
import muoipt.githubuser.network.utils.ApiUtils.tryCatchApiException
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

interface GitHubUserApiInterface {
    suspend fun getUsers(itemPerPage: Int, since: Int): List<GithubUser>
}

class GitHubUserApi @Inject constructor(
    private val githubUserApiService: GithubUserApiService
): GitHubUserApiInterface {
    override suspend fun getUsers(itemPerPage: Int, since: Int): List<GithubUser> {
        return tryCatchApiException { githubUserApiService.getUsers(itemPerPage, since) }
    }
}


// API from: https://api.github.com/users?per_page=20&since=100
interface GithubUserApiService {

    @GET("/users")
    suspend fun getUsers(
        @Query("per_page") perPage: Int,
        @Query("since") since: Int?
    ): List<GithubUser>
}