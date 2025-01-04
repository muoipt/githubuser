package muoipt.githubuser.data

import androidx.paging.PagingSource
import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import muoipt.githubuser.data.repositories.GithubUserRepo
import muoipt.githubuser.data.repositories.UsersRemoteMediator
import muoipt.githubuser.data.usecases.GetUserListUseCaseImpl
import muoipt.githubuser.model.GithubUserEntity
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.mockito.kotlin.verify

class GetUserListUseCaseImplTest {

    @Mock
    private lateinit var usersRemoteMediator: UsersRemoteMediator

    @Mock
    private lateinit var githubUserRepo: GithubUserRepo

    private lateinit var getUserListUseCase: GetUserListUseCaseImpl
    private lateinit var mockPagingSource: PagingSource<Int, GithubUserEntity>

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        mockPagingSource =
            Mockito.mock(PagingSource::class.java) as PagingSource<Int, GithubUserEntity>
        getUserListUseCase = GetUserListUseCaseImpl(usersRemoteMediator, githubUserRepo)
    }

    @Test
    fun `execute should call loadAllUsersPaged from githubUserRepo`() = runTest {
        val userEntity1 = GithubUserEntity(
            login = "testLogin1",
            avatarUrl = "testAvatarUrl1",
            htmlUrl = "testHtmlUrl1"
        )
        val userEntity2 = GithubUserEntity(
            login = "testLogin2",
            avatarUrl = "testAvatarUrl2",
            htmlUrl = "testHtmlUrl2"
        )
        val list = listOf(userEntity1, userEntity2)
        val mockPage = PagingSource.LoadResult.Page(data = list, prevKey = null, nextKey = 1)

        given(mockPagingSource.load(any())).willReturn(mockPage)
        given(githubUserRepo.loadAllUsersPaged()).willReturn(mockPagingSource)

        getUserListUseCase.execute().test {
            awaitItem()
            verify(githubUserRepo).loadAllUsersPaged()
        }
    }
}