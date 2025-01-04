package muoipt.githubuser.data

import muoipt.githubuser.data.mapper.toDataDetailModel
import muoipt.githubuser.data.mapper.toDataModel
import muoipt.githubuser.data.mapper.toEntity
import muoipt.githubuser.model.GithubUser
import muoipt.githubuser.model.GithubUserEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class GithubUserMapperTest {

    @Test
    fun testToEntity() {
        val githubUser = GithubUser(login = "testLogin", avatarUrl = "testAvatarUrl", htmlUrl = "testHtmlUrl")
        val githubUserEntity = githubUser.toEntity()
        assertEquals("testLogin", githubUserEntity.login)
        assertEquals("testAvatarUrl", githubUserEntity.avatarUrl)
        assertEquals("testHtmlUrl", githubUserEntity.htmlUrl)
    }

    @Test
    fun testToDataModel() {
        val githubUserEntity = GithubUserEntity(login = "testLogin", avatarUrl = "testAvatarUrl", htmlUrl = "testHtmlUrl")
        val githubUserData = githubUserEntity.toDataModel()
        assertEquals("testLogin", githubUserData.login)
        assertEquals("testAvatarUrl", githubUserData.avatarUrl)
        assertEquals("testHtmlUrl", githubUserData.htmlUrl)
    }

    @Test
    fun testToDataDetailModel() {
        val githubUserEntity = GithubUserEntity(
            login = "testLogin",
            avatarUrl = "testAvatarUrl",
            htmlUrl = "testHtmlUrl",
            location = "testLocation",
            followers = 100,
            following = 200
        )
        val githubUserDetailData = githubUserEntity.toDataDetailModel()
        assertEquals("testLogin", githubUserDetailData.login)
        assertEquals("testAvatarUrl", githubUserDetailData.avatarUrl)
        assertEquals("testHtmlUrl", githubUserDetailData.htmlUrl)
        assertEquals("testLocation", githubUserDetailData.location)
        assertEquals(100, githubUserDetailData.followers)
        assertEquals(200, githubUserDetailData.following)
    }
}