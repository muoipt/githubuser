package muoipt.githubuser

import app.cash.turbine.test
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import muoipt.githubuser.data.common.UserErrorCode
import muoipt.githubuser.data.usecases.GetUserDetailUseCase
import muoipt.githubuser.model.GithubUserDetailData
import muoipt.githubuser.screens.details.UserDetailViewModel
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

class UserDetailViewModelTest: BaseViewModelTest() {

    @Mock
    private lateinit var getUserDetailUseCase: GetUserDetailUseCase

    private lateinit var viewModel: UserDetailViewModel

    @Before
    fun setUp() {
        super.setUpBase()
        viewModel = UserDetailViewModel(getUserDetailUseCase, testDispatcher)
    }

    @Test
    fun testLoadUserDetailSuccess() = runTest {
        val userLogin = "test user"
        val userDetail = GithubUserDetailData(login = userLogin)
        Mockito.`when`(getUserDetailUseCase.getUserDetail(userLogin)).thenReturn(flow {
            emit(userDetail)
        })

        viewModel.uiState.test {
            skipItems(1) // skip first emit value of viewModel.uiState because it is default value
            viewModel.loadUserDetail(userLogin)
            val uiState = awaitItem()
            assert(!uiState.isLoading)
            assert(uiState.error == null)
            assert(uiState.userDetail == userDetail)
            expectNoEvents()
        }
    }

    @Test
    fun testLoadUserDetailError() = runTest {
        val userLogin = "test user"
        val errorMessage = "User not found"
        Mockito.`when`(getUserDetailUseCase.getUserDetail(userLogin)).thenReturn(flow {
            throw Exception(errorMessage)
        })

        viewModel.uiState.test {
            skipItems(1)
            viewModel.loadUserDetail(userLogin)
            val uiState = awaitItem()
            assert(!uiState.isLoading)
            assert(uiState.userDetail == GithubUserDetailData())
            assert(uiState.error?.errorCode == UserErrorCode.LoadUserException)
            assert(uiState.error?.errorMessage == errorMessage)
            expectNoEvents()
        }
    }

    @Test
    fun testLoadUserDetailNotFound() = runTest {
        val userLogin = "test user"
        Mockito.`when`(getUserDetailUseCase.getUserDetail(userLogin)).thenReturn(flow {
            emit(null)
        })

        viewModel.uiState.test {
            skipItems(1)
            viewModel.loadUserDetail(userLogin)
            val uiState = awaitItem()
            assert(!uiState.isLoading)
            assert(uiState.userDetail == GithubUserDetailData())
            assert(uiState.error?.errorCode == UserErrorCode.LoadUserNotFound)
            expectNoEvents()
        }
    }
}