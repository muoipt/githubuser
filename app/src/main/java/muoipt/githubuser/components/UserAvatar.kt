package muoipt.githubuser.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import muoipt.githubuser.ui.theme.Background
import muoipt.githubuser.ui.theme.Purple80
import muoipt.githubuser.ui.theme.Shapes


@Composable
fun UserAvatar(modifier: Modifier, avatarUrl: String){
    Box(
        modifier = modifier
            .background(color = Background, shape = Shapes.medium)
    ) {

        AsyncImage(
            model = avatarUrl,
            contentDescription = "user_avatar",
            modifier = Modifier
                .size(90.dp)
                .padding(4.dp)
                .clip(CircleShape)
                .background(color = Purple80, shape = CircleShape)

        )
    }
}