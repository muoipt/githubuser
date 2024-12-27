package muoipt.githubuser.screens.listing

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import muoipt.githubuser.components.CircleProgressBar
import muoipt.githubuser.data.common.AppLog
import muoipt.githubuser.model.GithubUserData

@Composable
fun ArticlesListingScreen(
    modifier: Modifier,
    viewModel: ArticlesListingMVViewModel = hiltViewModel(),
    onDetailClicked: (loginUser: String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val uiState = state as? UsersListingUIState

    val isLoading by remember(uiState?.isLoading) {
        derivedStateOf { uiState?.isLoading }
    }

    val error by remember(uiState?.error) {
        derivedStateOf { uiState?.error }
    }

    val usersList by remember(uiState?.users) {
        derivedStateOf { uiState?.users ?: listOf() }
    }

    if (isLoading == true) {
        CircleProgressBar()
    } else {
        if (error != null) {
            ErrorUI(modifier, error?.errorMessage)
        }

        if (usersList.isEmpty()) {
            EmptyUi(modifier)
        } else {
            SetupUi(modifier = modifier, usersList = usersList, onDetailClicked = {})
        }
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
        Text(text = "No article available")
    }
}

@Composable
private fun SetupUi(
    modifier: Modifier,
    usersList: List<GithubUserData>,
    onDetailClicked: (title: String) -> Unit,
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(usersList) {
        AppLog.listing("listState.firstVisibleItemIndex = ${listState.firstVisibleItemIndex}")
        coroutineScope.launch {
            listState.animateScrollToItem(
                listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset
            )
        }
    }

    LazyColumn(
        state = listState, modifier = modifier.fillMaxWidth()
    ) {
        items(usersList.size) { index ->
            ArticleItemView(usersList[index]) {
                onDetailClicked(it)
            }
        }
    }
}

@Composable
private fun ArticleItemView(
    usersListingUiData: GithubUserData,
    onDetailClicked: (title: String) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            onDetailClicked(usersListingUiData.login)
        }) {
        AsyncImage(
            model = usersListingUiData.avatarUrl, contentDescription = null, modifier = Modifier.fillMaxWidth()
        )
        Text(
            modifier = Modifier.padding(16.dp), color = Color.Black, text = usersListingUiData.login
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}