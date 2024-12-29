package muoipt.githubuser.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import muoipt.githubuser.R
import muoipt.githubuser.ui.theme.GitHubUserTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Appbar(
    currentScreenLabel: String?,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(title = { Text(currentScreenLabel ?: "") },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color.White
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        })
}

@Preview
@Composable
private fun AppbarPreviewForListingPage(){
    GitHubUserTheme {
        Appbar(
            currentScreenLabel = "GithubUser",
            canNavigateBack = false,
            navigateUp = {}
        )
    }
}

@Preview
@Composable
private fun AppbarPreviewForDetailPage(){
    GitHubUserTheme {
        Appbar(
            currentScreenLabel = "Back",
            canNavigateBack = true,
            navigateUp = {}
        )
    }
}