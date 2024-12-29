package muoipt.githubuser.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import muoipt.githubuser.screens.listing.UsersListingScreen1

@Composable
fun NavigationGraph(
    modifier: Modifier,
    navController: NavHostController,
) {

    NavHost(
        navController = navController,
        startDestination = ScreenRoute.UsersListing.route
    ) {
        composable(route = ScreenRoute.UsersListing.route) {
            UsersListingScreen1(modifier = modifier) {
                navController.navigate(ScreenRoute.UserDetail.route)
            }
        }

        composable(route = ScreenRoute.UserDetail.route) {

        }
    }
}