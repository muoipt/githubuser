package muoipt.githubuser.screens.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import muoipt.githubuser.components.CircleProgressBar
import muoipt.githubuser.model.GithubUserData
import muoipt.githubuser.model.GithubUserDetailData

@Composable
fun UserDetailScreen(
    modifier: Modifier = Modifier, viewModel: UserDetailViewModel = hiltViewModel(),
    userLogin: String
) {

    LaunchedEffect(userLogin) {
        viewModel.loadUserDetail(userLogin)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val isLoading by remember(uiState.isLoading) {
        derivedStateOf { uiState.isLoading }
    }

    val error by remember(uiState.error) {
        derivedStateOf { uiState.error }
    }

    val user by remember(uiState.userDetail) {
        derivedStateOf { uiState.userDetail }
    }

    if (isLoading) {
        CircleProgressBar()
    }

    if (error != null) {
        ErrorUI(modifier, error!!.message)
    }

    if (user.login.isBlank()) {
        EmptyUi(modifier)
    } else {
        SetupUi(modifier = modifier, userDetail = user)
    }
}

@Composable
private fun ErrorUI(modifier: Modifier, error: String?) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = error ?: "An error occurred!")
    }
}

@Composable
private fun EmptyUi(modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "No user detail data available")
    }
}

@Composable
private fun SetupUi(
    modifier: Modifier,
    userDetail: GithubUserDetailData
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        AsyncImage(
            model = userDetail.avatarUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            modifier = Modifier.padding(16.dp), color = Color.Black, text = userDetail.login
        )
        Text(
            modifier = Modifier.padding(16.dp), color = Color.Black, text = userDetail.location
        )

        Text(
            modifier = Modifier.padding(16.dp),
            color = Color.Black,
            text = userDetail.follower.toString()
        )

        Text(
            modifier = Modifier.padding(16.dp),
            color = Color.Black,
            text = userDetail.following.toString()
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun UserItem(user: GithubUserData, onDetailClicked: (loginUser: String) -> Unit) {
    // Your user item UI implementation
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            onDetailClicked(user.login)
        }) {
        AsyncImage(
            model = user.avatarUrl, contentDescription = null, modifier = Modifier.fillMaxWidth()
        )
        Text(
            modifier = Modifier.padding(16.dp), color = Color.Black, text = user.login
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}