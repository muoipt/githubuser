package muoipt.githubuser.data.common

enum class UserErrorCode {
    LoadUserException,
    LoadUserNotFound
}

class UserError(
    val errorCode: UserErrorCode,
    val errorMessage: String? = null
): Exception()