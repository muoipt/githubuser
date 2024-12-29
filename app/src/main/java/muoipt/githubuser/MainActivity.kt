package muoipt.githubuser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import muoipt.githubuser.components.Appbar
import muoipt.githubuser.navigation.NavigationGraph
import muoipt.githubuser.navigation.UserListingNavigation
import muoipt.githubuser.ui.theme.GitHubUserTheme

@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GitHubUserTheme {
                GithubUserApp()
            }
        }
    }
}

@Composable
private fun GithubUserApp(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute =
        if (backStackEntry?.destination?.route == UserListingNavigation.route) stringResource(
            id = R.string.app_name
        ) else stringResource(R.string.app_bar_detail)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Appbar(currentScreenLabel = currentRoute,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = {
                    navController.navigateUp()
                })
        }) { innerPadding ->
        NavigationGraph(
            modifier = Modifier.padding(innerPadding), navController
        )
    }
}