package muoipt.githubuser.screens.listing

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import muoipt.githubuser.components.CircleProgressBar
import muoipt.githubuser.components.CustomTabOpener
import muoipt.githubuser.components.UserAvatar
import muoipt.githubuser.data.common.AppLog
import muoipt.githubuser.model.GithubUserData
import muoipt.githubuser.ui.theme.Background
import muoipt.githubuser.ui.theme.Blue
import muoipt.githubuser.ui.theme.GitHubUserTheme
import muoipt.githubuser.ui.theme.Shapes


@Composable
fun UsersListingScreen(
    modifier: Modifier = Modifier,
    viewModel: UsersListingViewModel = hiltViewModel(),
    onDetailClicked: (loginUser: String) -> Unit
) {
    val usersPagingData = viewModel.usersPagingData.collectAsLazyPagingItems()
    AppLog.listing("UsersListingScreen1 usersPagingData = $usersPagingData")

    LazyColumn(modifier = modifier.fillMaxSize()) {
        AppLog.listing("Muoi123 => UsersListingScreen1 usersPagingData.itemCount = ${usersPagingData.itemCount}")

        items(
            count = usersPagingData.itemCount,
            key = usersPagingData.itemKey { it.login },
            contentType = usersPagingData.itemContentType { "user" }
        ) { index ->
            val user = usersPagingData[index]
            if (user != null) {
                UserCard(user, onDetailClicked)
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
private fun LoadingRow() {
    CircleProgressBar()
}

@Composable
private fun ErrorRow(errorMessage: String?) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = errorMessage ?: "An error occurred!")
    }
}

@Composable
private fun UserCard(user: GithubUserData, onDetailClicked: (userName: String) -> Unit) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .background(color = Color.White, shape = Shapes.medium)
            .clickable {
                onDetailClicked(user.login)
            }
    ) {
        val (avatar, userName, divider, url) = createRefs()
        val padding = 16.dp
        val smallPadding = 8.dp
        val context = LocalContext.current

        UserAvatar(
            modifier = Modifier
                .constrainAs(avatar) {
                    start.linkTo(parent.start, padding)
                    top.linkTo(parent.top, padding)
                    bottom.linkTo(parent.bottom, padding)
                },
            user.avatarUrl
        )

        Text(
            text = user.login,
            style = TextStyle(fontWeight = FontWeight.Bold),
            fontSize = 20.sp,
            modifier = Modifier.constrainAs(userName) {
                start.linkTo(avatar.end, padding)
                top.linkTo(parent.top, padding)
            }
        )
        Box(
            modifier = Modifier
                .height(1.dp)
                .background(color = Background)
                .constrainAs(divider) {
                    start.linkTo(avatar.end, padding)
                    end.linkTo(parent.end, padding)
                    top.linkTo(userName.bottom, smallPadding)
                    width = Dimension.fillToConstraints
                })
        Text(
            text = user.htmlUrl,
            fontSize = 12.sp,
            color = Blue,
            textDecoration = TextDecoration.Underline,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .constrainAs(url) {
                    start.linkTo(userName.start)
                    top.linkTo(divider.bottom, padding)
                }
                .clickable {
                    // open the webpage from url
                    CustomTabOpener.openCustomTabFromUrl(context, user.htmlUrl)
                }
        )
    }
}

@Preview
@Composable
private fun BlogCardPreview() {
    GitHubUserTheme {
        UserCard(
            user = GithubUserData(
                login = "User1",
                htmlUrl = "https://github.com/mojombo",
            )
        ) {}
    }
}