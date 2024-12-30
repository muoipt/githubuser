package muoipt.githubuser.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import muoipt.githubuser.R
import muoipt.githubuser.base.BaseUi
import muoipt.githubuser.components.CircleProgressBar
import muoipt.githubuser.components.CustomTabOpener
import muoipt.githubuser.components.UserAvatar
import muoipt.githubuser.model.GithubUserDetailData
import muoipt.githubuser.ui.theme.Background
import muoipt.githubuser.ui.theme.GitHubUserTheme
import muoipt.githubuser.ui.theme.Shapes
import muoipt.githubuser.ui.theme.Tertiary
import muoipt.githubuser.utils.getFollowNumber

@Composable
fun UserDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: UserDetailViewModel = hiltViewModel(),
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
    } else {
        BaseUi(
            modifier = modifier.background(color = Color.Red),
            content = {
                if (error != null) {
                    ErrorView(modifier, error!!.errorMessage)
                } else {
                    ContentView(userDetail = user)
                }
            }, snackBarMessage = error?.errorMessage
        )
    }
}

@Composable
private fun ErrorView(modifier: Modifier, error: String?) {
    Box(modifier = modifier
        .fillMaxSize()
        .padding(16.dp), contentAlignment = Alignment.Center) {
        Text(text = error ?: stringResource(R.string.error))
    }
}

@Composable
private fun ContentView(
    userDetail: GithubUserDetailData
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        UserCard(userDetail)
        FollowCard(userDetail)
        BlockCard(userDetail)
    }
}

@Composable
private fun BlockCard(userDetail: GithubUserDetailData) {
    val context = LocalContext.current
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = stringResource(R.string.blog),
            style = TextStyle(fontWeight = FontWeight.Bold),
            fontSize = 20.sp,
        )

        Text(
            modifier = Modifier
                .padding(top = 8.dp)
                .clickable {
                    // open the webpage from url
                    CustomTabOpener.openCustomTabFromUrl(context, userDetail.htmlUrl)
                },
            text = userDetail.htmlUrl,
            fontSize = 18.sp,
            color = Tertiary
        )
    }
}

@Composable
private fun FollowCard(userDetail: GithubUserDetailData) {
    Row(modifier = Modifier.padding(horizontal = 24.dp)) {
        FollowItem(
            modifier = Modifier.weight(1f),
            number = getFollowNumber(userDetail.follower),
            FollowType.FOLLOWER
        )
        FollowItem(
            modifier = Modifier.weight(1f),
            number = getFollowNumber(userDetail.following),
            FollowType.FOLLOWING
        )
    }
}

@Composable
private fun FollowItem(modifier: Modifier, number: String, followType: FollowType) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(color = Background, shape = CircleShape)
                .padding(16.dp)
        ) {
            Icon(
                painter = if (followType == FollowType.FOLLOWER)
                    painterResource(id = R.drawable.ic_follower)
                else painterResource(
                    id = R.drawable.ic_following
                ),
                contentDescription = "follower_icon",
                modifier = Modifier
                    .size(36.dp)
                    .background(color = Background)
            )
        }

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = number,
            style = TextStyle(fontWeight = FontWeight.Bold),
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = followType.type,
            fontSize = 16.sp,
            color = Tertiary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun UserCard(userDetail: GithubUserDetailData) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .background(color = Color.White, shape = Shapes.medium)
    ) {
        val (avatar, userName, divider, locationIcon, location) = createRefs()
        val padding = 16.dp
        val smallPadding = 8.dp

        UserAvatar(
            modifier = Modifier
                .constrainAs(avatar) {
                    start.linkTo(parent.start, padding)
                    top.linkTo(parent.top, padding)
                    bottom.linkTo(parent.bottom, padding)
                },
            userDetail.avatarUrl
        )

        Text(
            text = userDetail.login,
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
        Icon(
            painter = painterResource(id = R.drawable.ic_location),
            contentDescription = "user_location",
            tint = Tertiary,
            modifier = Modifier
                .size(20.dp)
                .constrainAs(locationIcon) {
                    start.linkTo(userName.start)
                    top.linkTo(divider.bottom, smallPadding)
                },
        )
        Text(
            text = userDetail.location,
            fontSize = 12.sp,
            color = Tertiary,
            modifier = Modifier.constrainAs(location) {
                start.linkTo(locationIcon.end, smallPadding)
                top.linkTo(locationIcon.top)
                bottom.linkTo(locationIcon.bottom)
            }
        )
    }
}

enum class FollowType(val type: String) {
    FOLLOWER("Follower"),
    FOLLOWING("Following")
}

@Preview
@Composable
private fun UserCardPreview() {
    GitHubUserTheme {
        UserCard(
            userDetail = GithubUserDetailData(
                login = "User1",
                avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
                location = "Vietnam"
            )
        )
    }
}

@Preview
@Composable
private fun FollowItemPreview() {
    GitHubUserTheme {
        FollowCard(
            userDetail = GithubUserDetailData(
                login = "User1",
                avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
                location = "Vietnam",
                follower = 100,
                following = 21
            )
        )
    }
}

@Preview
@Composable
private fun BlogCardPreview() {
    GitHubUserTheme {
        BlockCard(
            userDetail = GithubUserDetailData(
                login = "User1",
                htmlUrl = "https://avatars.githubusercontent.com/u/1?v=4",
                location = "Vietnam",
                follower = 100,
                following = 21
            )
        )
    }
}

