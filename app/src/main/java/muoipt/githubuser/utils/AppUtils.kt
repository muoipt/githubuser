package muoipt.githubuser.utils

fun getFollowNumber(number: Int): String {
    return when {
        number < 10 -> number.toString()
        number in 10..99 -> "10+"
        number in 100..999 -> "100+"
        number in 1000..9999 -> "1000+"
        number in 10000..99999 -> "10000+"
        number in 100000..999999 -> "100000+"
        number in 1000000..9999999 -> "1000000+"
        else -> "10000000+"
    }
}
