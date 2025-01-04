package muoipt.githubuser.model

data class GithubUserData(
    val login: String = "",
    val avatarUrl: String = "",
    val htmlUrl: String = "",
)


data class GithubUserDetailData(
    val login: String = "",
    val avatarUrl: String = "",
    val htmlUrl: String = "",
    val location: String = "",
    val followers: Int = 0,
    val following: Int = 0
)