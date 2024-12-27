package muoipt.githubuser.data.mapper

import muoipt.githubuser.model.GithubUser
import muoipt.githubuser.model.GithubUserData
import muoipt.githubuser.model.GithubUserDetailData
import muoipt.githubuser.model.GithubUserDetail
import muoipt.githubuser.model.GithubUserEntity


fun GithubUser.toEntity() = GithubUserEntity(
    login = login,
    avatar_url = avatarUrl,
    html_url = htmlUrl
)

fun GithubUserDetail.toEntity() = GithubUserEntity(
    login = login,
    avatar_url = avatarUrl,
    html_url = htmlUrl,
    location = location,
    followers = followers,
    following = following
)

fun GithubUserEntity.toDataModel() = GithubUserData(
    login = login,
    avatarUrl = avatar_url,
    htmlUrl = html_url
)

fun GithubUserEntity.toDataDetailModel() = GithubUserDetailData(
    login = login,
    avatarUrl = avatar_url,
    htmlUrl = html_url,
    location = location,
    follower = followers,
    following = following
)
