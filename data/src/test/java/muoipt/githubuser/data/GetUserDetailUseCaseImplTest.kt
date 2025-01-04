package muoipt.githubuser.data

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import muoipt.githubuser.data.common.DataStrategy
import muoipt.githubuser.data.repositories.GithubUserRepo
import muoipt.githubuser.data.usecases.GetUserDetailUseCaseImpl
import muoipt.githubuser.model.GithubUserDetailData
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class GetUserDetailUseCaseImplTest {

    @Mock
    private lateinit var githubUserRepo: GithubUserRepo

    private lateinit var getUserDetailUseCase: GetUserDetailUseCaseImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getUserDetailUseCase = GetUserDetailUseCaseImpl(githubUserRepo)
    }

    @Test
    fun `getUserDetail should return user detail data`() = runTest {
        val userLogin = "testuser"
        val userDetail = GithubUserDetailData(login = userLogin)
        Mockito.`when`(githubUserRepo.getUserDetail(DataStrategy.LOCAL, userLogin)).thenReturn(flow {
            emit(userDetail)
        })

        val result = getUserDetailUseCase.getUserDetail(userLogin)
        result.collect { data ->
            assertEquals(userDetail, data)
        }
    }

    @Test
    fun `getUserDetail should return null when user not found`() = runTest {
        val userLogin = "testuser"
        Mockito.`when`(githubUserRepo.getUserDetail(DataStrategy.LOCAL, userLogin)).thenReturn(flow {
            emit(null)
        })

        val result = getUserDetailUseCase.getUserDetail(userLogin)
        result.collect { data ->
            assertEquals(null, data)
        }
    }
}