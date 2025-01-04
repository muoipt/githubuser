package muoipt.githubuser

import muoipt.githubuser.utils.getFollowNumber
import org.junit.Assert.assertEquals
import org.junit.Test

class AppUtilsTest {

    @Test
    fun testGetFollowNumber() {
        assertEquals("0", getFollowNumber(0))
        assertEquals("9", getFollowNumber(9))
        assertEquals("10+", getFollowNumber(10))
        assertEquals("10+", getFollowNumber(99))
        assertEquals("100+", getFollowNumber(100))
        assertEquals("100+", getFollowNumber(999))
        assertEquals("1000+", getFollowNumber(1000))
        assertEquals("1000+", getFollowNumber(9999))
        assertEquals("10000+", getFollowNumber(10000))
        assertEquals("10000+", getFollowNumber(99999))
        assertEquals("100000+", getFollowNumber(100000))
        assertEquals("100000+", getFollowNumber(999999))
        assertEquals("1000000+", getFollowNumber(1000000))
        assertEquals("1000000+", getFollowNumber(9999999))
        assertEquals("10000000+", getFollowNumber(10000000))
    }
}