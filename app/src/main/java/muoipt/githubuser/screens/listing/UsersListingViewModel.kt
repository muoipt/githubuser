package muoipt.githubuser.screens.listing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import muoipt.githubuser.data.usecases.GetUserListUseCase
import javax.inject.Inject

@HiltViewModel
class UsersListingViewModel @Inject constructor(
    getUserListUseCase: GetUserListUseCase,
): ViewModel() {
    val usersPagingData = getUserListUseCase.execute().cachedIn(viewModelScope)
}