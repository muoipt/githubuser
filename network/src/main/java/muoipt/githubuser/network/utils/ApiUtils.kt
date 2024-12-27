package muoipt.githubuser.network.utils

import org.json.JSONObject
import retrofit2.HttpException

object ApiUtils {
    suspend fun <T> tryCatchApiException(block: suspend () -> T): T {
        return try {
            block()
        } catch (exception: Exception) {
            if (exception is HttpException) {
                throw ApiException.from(exception)
            }
            throw exception
        }
    }
}

class ApiException(
    message: String
): Exception(message) {
    companion object {
        fun from(exception: HttpException): ApiException {
            val rawCause = exception.response()?.errorBody()?.string().orEmpty()
            val json = JSONObject(rawCause)
            val cause = json.getString(KEY_ERROR) as String

            return ApiException(
                message = cause,
            )
        }
    }
}

private const val KEY_ERROR = "error"