package muoipt.githubuser.screens.listing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import muoipt.githubuser.data.common.AppLog
import muoipt.githubuser.data.usecases.GetUserListUseCase
import muoipt.githubuser.model.GithubUserData
import javax.inject.Inject


//todo change to extend from BaseViewModel
@HiltViewModel
class UsersListingViewModel @Inject constructor(
    private val getUserListUseCase: GetUserListUseCase,
): ViewModel() {
    private var _uiState: MutableStateFlow<UsersListingUIState> =
        MutableStateFlow(UsersListingUIState())
    val uiState: StateFlow<UsersListingUIState> = _uiState.asStateFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, UsersListingUIState())


    val usersPagingData = loadUserWithPaging()

    fun loadUserWithPaging(): Flow<PagingData<GithubUserData>> {
        AppLog.listing("UsersListingViewModel loadUserWithPaging")
        return getUserListUseCase.getUser().cachedIn(viewModelScope)
    }

//    fun loadUsers() {
//        viewModelScope.launch {
//            getUserListUseCase.getUser()
//                .onStart {
//                    _uiState.update {
//                        it.copy(isLoading = true)
//                    }
//                }.catch { ex ->
//                    AppLog.listing("loadArticles ex = $ex")
//
//                    _uiState.update {
//                        it.copy(
//                            isLoading = false,
//                            error = UserError(UserErrorCode.LoadUserException, ex.message)
//                        )
//                    }
//                }.collect { data ->
//                    _uiState.update {
//                        it.copy(isLoading = false, usersPaging = data)
//                    }
//                }
//        }
//    }
}