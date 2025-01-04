package muoipt.githubuser.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import muoipt.githubuser.database.dao.RemoteKeysDaoImpl
import muoipt.githubuser.model.RemoteKeysEntity
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RemoteKeysDaoImplTest {

    private lateinit var db: UserDb
    private lateinit var remoteKeysDao: RemoteKeysDaoImpl

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, UserDb::class.java).build()
        remoteKeysDao = db.remoteKeysDao() as RemoteKeysDaoImpl
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testRemoteKeysByUserLogin() = runBlocking {
        val testUserLogin = "test user login"
        val remoteKey = RemoteKeysEntity(testUserLogin, null, 1)
        remoteKeysDao.insert(remoteKey)
        val retrievedKey = remoteKeysDao.remoteKeysByUserLogin(testUserLogin)
        assertNotNull(retrievedKey)
        assertEquals(remoteKey.userLogin, retrievedKey?.userLogin)
    }

    @Test
    fun testClearRemoteKeys() = runBlocking {
        val testUserLogin = "test user login"
        val remoteKey = RemoteKeysEntity(testUserLogin, null, 1)
        remoteKeysDao.insert(remoteKey)
        remoteKeysDao.clearRemoteKeys()
        val retrievedKey = remoteKeysDao.remoteKeysByUserLogin(testUserLogin)
        assertNull(retrievedKey)
    }
}