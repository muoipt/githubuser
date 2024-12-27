package muoipt.githubuser

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GitHubUserApplication: Application() {
    companion object {
        private lateinit var instance: GitHubUserApplication
        fun instance(): GitHubUserApplication {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}