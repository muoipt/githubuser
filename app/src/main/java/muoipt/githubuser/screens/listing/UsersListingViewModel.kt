package muoipt.githubuser.screens.listing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import muoipt.githubuser.common.IoDispatcher
import muoipt.githubuser.data.usecases.GetUserListUseCase
import javax.inject.Inject

@HiltViewModel
class UsersListingViewModel @Inject constructor(
    getUserListUseCase: GetUserListUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): ViewModel() {
    val usersPagingData = getUserListUseCase.execute()
        .cachedIn(viewModelScope)
        .flowOn(ioDispatcher)
}