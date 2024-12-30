package muoipt.githubuser.utils

import android.content.Context
import android.net.ConnectivityManager
import muoipt.githubuser.GitHubUserApplication


object ConnectivityUtils {
    fun isConnected(): Boolean {
        val context = GitHubUserApplication.instance()
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetwork != null
            && cm.getNetworkCapabilities(cm.activeNetwork) != null
    }
}