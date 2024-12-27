package muoipt.githubuser.screens.listing

import muoipt.githubuser.base.UIAction
import muoipt.githubuser.data.common.UserError
import muoipt.githubuser.model.GithubUserData


sealed class UsersListingAction: UIAction {
    data object LoadUsers: UsersListingAction()
    data object LoadMore: UsersListingAction()
}

data class UsersListingUIState(
    val isLoading: Boolean = false,
    val error: UserError? = null,
    val users: List<GithubUserData> = listOf()
)
