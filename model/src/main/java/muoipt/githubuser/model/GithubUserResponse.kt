package muoipt.githubuser.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GithubUser(
    @field:Json(name = "login") val login: String = "",
    @field:Json(name = "avatar_url") val avatarUrl: String = "",
    @field:Json(name = "html_url") val htmlUrl: String = "",
)

@JsonClass(generateAdapter = true)
data class GithubUserDetail(
    @field:Json(name = "login") val login: String = "",
    @field:Json(name = "avatar_url") val avatarUrl: String = "",
    @field:Json(name = "html_url") val htmlUrl: String = "",
    @field:Json(name = "location") val location: String = "",
    @field:Json(name = "followers") val followers: Int = 0,
    @field:Json(name = "following") val following: Int = 0,
)

