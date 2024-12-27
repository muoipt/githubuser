package muoipt.githubuser.data.common

import android.util.Log
import muoipt.githubuser.network.BuildConfig

enum class AppLogTag(val tag: String) {
    DEFAULT("nyt-app"),
    USERS_LISTING("users_listing"),
    USER_DETAIL("user_detail")
}

object AppLog {

    fun listing(message: String, tag: AppLogTag = AppLogTag.USERS_LISTING) {
        if (BuildConfig.DEBUG) {
            Log.e(tag.tag, message)
        }
    }

    fun detail(message: String, tag: AppLogTag = AppLogTag.USER_DETAIL) {
        if (BuildConfig.DEBUG) {
            Log.e(tag.tag, message)
        }
    }
}