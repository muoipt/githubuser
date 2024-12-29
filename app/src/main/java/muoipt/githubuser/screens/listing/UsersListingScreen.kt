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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import muoipt.githubuser.components.CircleProgressBar
import muoipt.githubuser.data.common.AppLog
import muoipt.githubuser.model.GithubUserData


@Composable
fun UsersListingScreen1(
    modifier: Modifier = Modifier,
    viewModel: UsersListingViewModel = hiltViewModel(),
    onDetailClicked: (loginUser: String) -> Unit
) {
    val usersPagingData = viewModel.usersPagingData.collectAsLazyPagingItems()
    AppLog.listing("UsersListingScreen1 usersPagingData = $usersPagingData")

    LazyColumn(modifier = modifier.fillMaxSize()) {
        AppLog.listing("UsersListingScreen1 usersPagingData.itemCount = ${usersPagingData.itemCount}")

        items(
            count = usersPagingData.itemCount,
            key = usersPagingData.itemKey { it.login },
            contentType = usersPagingData.itemContentType { it.login }
        ) { index ->
            val user = usersPagingData[index]
            if (user != null) {
                UserItem(user, onDetailClicked)
            }
        }

        usersPagingData.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    AppLog.listing("UsersListingScreen1 loadState.refresh is LoadState.Loading")
                    item { LoadingRow() }
                }

                loadState.append is LoadState.Loading -> {
                    AppLog.listing("UsersListingScreen1 loadState.append is LoadState.Loading")

                    item { LoadingRow() }
                }

                loadState.refresh is LoadState.Error -> {
                    AppLog.listing("UsersListingScreen1 loadState.refresh is LoadState.Error")

                    val e = loadState.refresh as LoadState.Error
                    item { ErrorRow(e.error.localizedMessage) }
                }

                loadState.append is LoadState.Error -> {
                    AppLog.listing("UsersListingScreen1 loadState.append is LoadState.Error")

                    val e = loadState.append as LoadState.Error
                    item { ErrorRow(e.error.localizedMessage) }
                }
            }
        }
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

@Composable
fun LoadingRow() {
    // Your loading row UI implementation
    CircleProgressBar()
}

@Composable
fun ErrorRow(errorMessage: String?) {
    // Your error row UI implementation
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = errorMessage ?: "An error occurred!")
    }
}