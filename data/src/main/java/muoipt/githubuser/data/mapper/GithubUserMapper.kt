package muoipt.githubuser.data.mapper

import muoipt.githubuser.model.GithubUser
import muoipt.githubuser.model.GithubUserData
import muoipt.githubuser.model.GithubUserDetail
import muoipt.githubuser.model.GithubUserDetailData
import muoipt.githubuser.model.GithubUserEntity


fun GithubUser.toEntity() = GithubUserEntity(
    login = login,
    avatarUrl = avatarUrl ?: "",
    htmlUrl = htmlUrl ?: ""
)

fun GithubUserDetail.toDataModel() = GithubUserDetailData(
    login = login,
    avatarUrl = avatarUrl ?: "",
    htmlUrl = htmlUrl ?: "",
    location = location ?: "",
    followers = followers ?: 0,
    following = following ?: 0
)

fun GithubUserEntity.toDataModel() = GithubUserData(
    login = login,
    avatarUrl = avatarUrl,
    htmlUrl = htmlUrl
)

fun GithubUserEntity.toDataDetailModel() = GithubUserDetailData(
    login = login,
    avatarUrl = avatarUrl,
    htmlUrl = htmlUrl,
    location = location,
    followers = followers,
    following = following
)
