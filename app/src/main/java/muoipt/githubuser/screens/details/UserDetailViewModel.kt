package muoipt.githubuser.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import muoipt.githubuser.data.common.UserError
import muoipt.githubuser.data.common.UserErrorCode
import muoipt.githubuser.data.usecases.GetUserDetailUseCase
import muoipt.githubuser.model.GithubUserDetailData
import javax.inject.Inject

data class UserDetailUIState(
    val isLoading: Boolean = false,
    val error: UserError? = null,
    val userDetail: GithubUserDetailData = GithubUserDetailData()
)

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val getUserDetailUseCase: GetUserDetailUseCase,
): ViewModel() {
    private var _uiState: MutableStateFlow<UserDetailUIState> =
        MutableStateFlow(UserDetailUIState())
    val uiState: StateFlow<UserDetailUIState> = _uiState.asStateFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, UserDetailUIState())

    fun loadUserDetail(login: String) {
        viewModelScope.launch {
            getUserDetailUseCase.getUserDetail(login).onStart {
                _uiState.update {
                    it.copy(isLoading = true)
                }
            }.catch { exception ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = UserError(UserErrorCode.LoadUserException, exception.message)
                    )
                }
            }.collect { data ->
                if (data == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = UserError(UserErrorCode.LoadUserNotFound)
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(isLoading = false, userDetail = data ?: GithubUserDetailData())
                    }
                }
            }
        }
    }
}