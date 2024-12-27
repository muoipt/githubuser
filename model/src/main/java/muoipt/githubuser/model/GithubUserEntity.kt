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
    val avatar_url: String = "",

    @ColumnInfo(name = "html_url")
    val html_url: String = "",

    @ColumnInfo(name = "location")
    val location: String = "",

    @ColumnInfo(name = "followers")
    val followers: Int = 0,

    @ColumnInfo(name = "following")
    val following: Int = 0
)