package muoipt.githubuser.navigation

import android.net.Uri
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import muoipt.githubuser.components.UserNavigationDestination
import muoipt.githubuser.screens.details.UserDetailScreen

object UserDetailNavigation: UserNavigationDestination {
    const val loginArg = "loginArg"

    override val destination = "user_detail_destination"

    override val route = "$destination?$loginArg={$loginArg}"

    fun createRoute(login: String): String {
        val encodedLogin = Uri.encode(login)
        return "$destination?$loginArg=$encodedLogin"
    }

    fun getLogin(entry: NavBackStackEntry) = entry.arguments?.getString(loginArg)
        ?: throw IllegalArgumentException("login user is required")
}

fun NavGraphBuilder.userDetail(modifier: Modifier,) {
    composable(
        route = UserDetailNavigation.route,
        arguments = listOf(
            navArgument(UserDetailNavigation.loginArg) {type = NavType.StringType}
        )
    ) { navBackStackEntry ->
        val login = UserDetailNavigation.getLogin(navBackStackEntry)
        UserDetailScreen(modifier = modifier, userLogin = login)
    }
}