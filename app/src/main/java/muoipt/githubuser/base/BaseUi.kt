package muoipt.githubuser.base

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import muoipt.githubuser.R
import muoipt.githubuser.components.AppSnackBar
import muoipt.githubuser.utils.ConnectivityUtils

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BaseUi(
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit,
    snackBarMessage: String? = null,
    infoIcon: Int? = null,
    backgroundColor: Color? = null,
    action: (() -> Unit)? = null
) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()

    val context = LocalContext.current
    val appSnackBarMessage = getErrorMessage(context, snackBarMessage)

    Scaffold(
        modifier = modifier, scaffoldState = scaffoldState, snackbarHost = {
            if (appSnackBarMessage?.isNotEmpty() == true) {
                AppSnackBar(
                    scaffoldState,
                    appSnackBarMessage,
                    infoIcon ?: R.drawable.ic_error_white,
                    backgroundColor ?: Color.Red,
                    action
                )
            }
        }, content = content
    )
}

private fun getErrorMessage(context: Context, snackBarMessage: String?): String? {
    return if (!ConnectivityUtils.isConnected()) {
        context.getString(R.string.error_no_internet_connection)
    } else {
        snackBarMessage
    }
}