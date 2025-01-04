package muoipt.githubuser.network.api

import muoipt.githubuser.model.GithubUser
import muoipt.githubuser.model.GithubUserDetail
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject

interface GitHubUserApiInterface {
    suspend fun getUsers(itemPerPage: Int, since: Int): List<GithubUser>
    suspend fun getByUserLogin(userName: String): GithubUserDetail
}

class GitHubUserApi @Inject constructor(
    private val githubUserApiService: GithubUserApiService
): GitHubUserApiInterface {
    override suspend fun getUsers(itemPerPage: Int, since: Int): List<GithubUser> {
        return githubUserApiService.getUsers(itemPerPage, since)
    }

    override suspend fun getByUserLogin(userName: String): GithubUserDetail {
        return githubUserApiService.getByUserLogin(userName)
    }
}


interface GithubUserApiService {

    // API from: https://api.github.com/users?per_page=20&since=100
    @GET("/users")
    suspend fun getUsers(
        @Query("per_page") perPage: Int,
        @Query("since") since: Int?
    ): List<GithubUser>

    // API from: https://api.github.com/users/{login_username}
    @GET("/users/{login_username}")
    suspend fun getByUserLogin(
        @Path("login_username") login: String
    ): GithubUserDetail
}