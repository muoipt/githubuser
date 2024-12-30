package muoipt.githubuser.navigation

import android.net.Uri
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import muoipt.githubuser.screens.details.UserDetailScreen

object UserDetailNavigation: UserNavigationDestination {
    const val LOGIN_ARG = "loginArg"

    override val destination = "user_detail_destination"

    override val route = "$destination?$LOGIN_ARG={$LOGIN_ARG}"

    fun createRoute(login: String): String {
        val encodedLogin = Uri.encode(login)
        return "$destination?$LOGIN_ARG=$encodedLogin"
    }

    fun getLogin(entry: NavBackStackEntry) = entry.arguments?.getString(LOGIN_ARG)
        ?: throw IllegalArgumentException("login user is required")
}

fun NavGraphBuilder.userDetail(modifier: Modifier,) {
    composable(
        route = UserDetailNavigation.route,
        arguments = listOf(
            navArgument(UserDetailNavigation.LOGIN_ARG) {type = NavType.StringType}
        )
    ) { navBackStackEntry ->
        val login = UserDetailNavigation.getLogin(navBackStackEntry)
        UserDetailScreen(modifier = modifier, userLogin = login)
    }
}