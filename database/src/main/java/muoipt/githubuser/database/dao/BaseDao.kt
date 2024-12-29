package muoipt.githubuser.database.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import androidx.room.Update

interface BaseDao<in T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg entity: T): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<T>): List<Long>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(entity: T)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(vararg entity: T)

}

@Transaction
suspend fun <T> BaseDao<T>.upsert(entity: T) {
    if (insert(entity) == -1L) {
        update(entity)
    }
}

@Transaction
suspend inline fun <reified T> BaseDao<T>.upsert(entities: List<T>) {
    val result = insert(*entities.toTypedArray())
    if (result.contains(-1L)) {
        val updateEntities = entities.mapIndexedNotNull { index, entity ->
            if (result[index] == -1L) entity else null
        }
        update(*updateEntities.toTypedArray())
    }
}