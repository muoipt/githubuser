package muoipt.githubuser.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import muoipt.githubuser.database.dao.RemoteKeysDaoImpl
import muoipt.githubuser.database.dao.UserDaoImpl
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDbTest {

    private lateinit var db: UserDb
    private lateinit var userDao: UserDaoImpl
    private lateinit var remoteKeysDao: RemoteKeysDaoImpl

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, UserDb::class.java).build()
        userDao = db.userDao()
        remoteKeysDao = db.remoteKeysDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testDatabaseCreated() {
        assertNotNull(db)
        assertNotNull(userDao)
        assertNotNull(remoteKeysDao)
    }
}