package muoipt.githubuser.screens.listing

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
import muoipt.githubuser.data.common.AppLog
import muoipt.githubuser.data.common.UserError
import muoipt.githubuser.data.common.UserErrorCode
import muoipt.githubuser.data.usecases.GetUserListUseCase
import javax.inject.Inject


//todo change to extend from BaseViewModel
@HiltViewModel
class UsersListingViewModel @Inject constructor(
    private val getUserListUseCase: GetUserListUseCase
): ViewModel() {
    private var _uiState: MutableStateFlow<UsersListingUIState> =
        MutableStateFlow(UsersListingUIState())
    val uiState: StateFlow<UsersListingUIState> = _uiState.asStateFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, UsersListingUIState())

    init {
        loadArticles()
    }

    private fun loadArticles() {
        viewModelScope.launch {
            getUserListUseCase.getUser(true)
                .onStart {
                    _uiState.update {
                        it.copy(isLoading = true)
                    }
                }.catch { ex ->
                    AppLog.listing("loadArticles ex = ${ex}")

                    _uiState.update {
                        it.copy(isLoading = false, error = UserError(UserErrorCode.LoadUserException, ex.message))
                    }
                }.collect { d ->
                    _uiState.update {
                        it.copy(isLoading = false, users = d)
                    }
                }
        }
    }

    private fun loadMore() {
        // todo
    }
}