package muoipt.githubuser.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import muoipt.githubuser.components.UserNavigationDestination
import muoipt.githubuser.screens.listing.UsersListingScreen1

object UserListingNavigation: UserNavigationDestination {

    override val destination = "user_listing_destination"

    override val route = destination

}

fun NavGraphBuilder.userListing(modifier: Modifier, navController: NavHostController) {
    composable(
        route = UserListingNavigation.route,
    ) {
        UsersListingScreen1(modifier = modifier) { userLogin->
            navController.navigate(UserDetailNavigation.createRoute(userLogin))
        }
    }
}