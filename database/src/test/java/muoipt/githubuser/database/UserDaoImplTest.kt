package muoipt.githubuser.database

import android.content.Context
import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import muoipt.githubuser.database.dao.UserDaoImpl
import muoipt.githubuser.model.GithubUserEntity
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDaoImplTest {

    private lateinit var db: UserDb
    private lateinit var userDao: UserDaoImpl

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, UserDb::class.java).build()
        userDao = db.userDao() as UserDaoImpl
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testGetByUserLogin() = runBlocking {
        val testLoginUser = "test login"
        val user =
            GithubUserEntity(testLoginUser, "Test Avatar", "Test html", "Test Location", 10, 5)
        userDao.insert(user)
        val retrievedUser = userDao.getByUserLogin(testLoginUser).first()
        assertNotNull(retrievedUser)
        assertEquals(user.login, retrievedUser?.login)
    }

    @Test
    fun testCount() = runBlocking {
        val user = GithubUserEntity("test_login", "Test User", "Test html", "Test Location", 10, 5)
        userDao.insert(user)
        val count = userDao.count()
        assertEquals(1, count)
    }

    @Test
    fun testDeleteAll() = runBlocking {
        val user = GithubUserEntity("test_login", "Test User", "Test html", "Test Location", 10, 5)
        userDao.insert(user)
        val countInsert = userDao.count()
        assertEquals(1, countInsert)

        userDao.deleteAll()
        val countDelete = userDao.count()
        assertEquals(0, countDelete)
    }

    @Test
    fun testUpdateUser() = runBlocking {
        val testUserLogin = "test user login"
        val user = GithubUserEntity(testUserLogin, "Test User", "Test html", "Test Location", 10, 5)
        userDao.insert(user)
        val newLocation = "New location"
        userDao.updateUser(testUserLogin, newLocation, 20, 10)
        val updatedUser = userDao.getByUserLogin(testUserLogin).first()
        assertNotNull(updatedUser)
        assertEquals(newLocation, updatedUser?.location)
        assertEquals(20, updatedUser?.followers)
        assertEquals(10, updatedUser?.following)
    }

    @Test
    fun testLoadAllUserPaged() = runBlocking {
        val user1 =
            GithubUserEntity("test_login1", "Test User 1", "Test html", "Test Location 1", 10, 5)
        val user2 =
            GithubUserEntity("test_login2", "Test User 2", "Test html", "Test Location 2", 20, 15)
        userDao.insert(user1)
        userDao.insert(user2)
        val pagingSource = userDao.loadAllUserPaged()
        val loadParams = PagingSource.LoadParams.Refresh<Int>(0, 10, false)
        val loadResult =
            pagingSource.load(loadParams) as PagingSource.LoadResult.Page<Int, GithubUserEntity>
        val users = loadResult.data
        assertEquals(2, users.size)
        assertEquals(user1, users[0])
        assertEquals(user2, users[1])
    }
}