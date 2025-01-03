package muoipt.githubuser.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import muoipt.githubuser.screens.listing.UsersListingScreen

object UserListingNavigation: UserNavigationDestination {

    override val destination = "user_listing_destination"

    override val route = destination

}

fun NavGraphBuilder.userListing(modifier: Modifier, navController: NavHostController) {
    composable(
        route = UserListingNavigation.route,
    ) {
        UsersListingScreen(modifier = modifier) { userLogin->
            navController.navigate(UserDetailNavigation.createRoute(userLogin))
        }
    }
}