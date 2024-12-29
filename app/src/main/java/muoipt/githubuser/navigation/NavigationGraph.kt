package muoipt.githubuser.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun NavigationGraph(
    modifier: Modifier,
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = UserListingNavigation.route
    ) {
        userListing(modifier, navController)

        userDetail(modifier)
    }
}