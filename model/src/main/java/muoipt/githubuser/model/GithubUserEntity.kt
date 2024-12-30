package muoipt.githubuser.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "github_user",
)
data class GithubUserEntity(
    @PrimaryKey
    @ColumnInfo(name = "login")
    val login: String = "",

    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String = "",

    @ColumnInfo(name = "html_url")
    val htmlUrl: String = "",

    @ColumnInfo(name = "location")
    val location: String = "",

    @ColumnInfo(name = "followers")
    val followers: Int = 0,

    @ColumnInfo(name = "following")
    val following: Int = 0
) {
    fun isFetchedWithDetail() = location.isNotEmpty() || followers != 0 || following != 0
}