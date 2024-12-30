package muoipt.githubuser.navigation

interface UserNavigationDestination {
    val route: String

    /**
     * To be used for compare and share between screens.
     */
    val destination: String

}