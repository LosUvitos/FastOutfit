package com.uvitos

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.uvitos.fastoutfit.data.database.AppDatabase
import com.uvitos.fastoutfit.data.database.FavoriteOutfit
import com.uvitos.fastoutfit.data.database.FavoriteOutfitDao
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoriteOutfitDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: FavoriteOutfitDao

    private val outfit1 = FavoriteOutfit(
        id = 0,
        shirtId = 1,
        pantId = 2,
        upperId = 3,
        shoesId = 4
    )

    private val outfit2 = FavoriteOutfit(
        id = 0,
        shirtId = 5,
        pantId = null,
        upperId = 6,
        shoesId = null
    )

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.favoriteOutfitDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insert_returnsPositiveRowId_andItemAppearsInGetAll() = runTest {
        val rowId = dao.insert(outfit1)
        assertTrue("insert debe devolver rowId > 0", rowId > 0)

        dao.getAll().test {
            val items = awaitItem()
            assertEquals(1, items.size)
            assertEquals(1, items.first().shirtId)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun insert_multipleItems_allAppearInGetAll() = runTest {
        dao.insert(outfit1)
        dao.insert(outfit2)

        dao.getAll().test {
            val items = awaitItem()
            assertEquals(2, items.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getAll_returnsItemsOrderedByCreatedAtDesc() = runTest {
        val early = FavoriteOutfit(
            shirtId = 1, pantId = 2, upperId = 3, shoesId = 4,
            createdAt = 1000L
        )
        val late = FavoriteOutfit(
            shirtId = 5, pantId = null, upperId = 6, shoesId = null,
            createdAt = 2000L
        )
        val firstId = dao.insert(early)
        val secondId = dao.insert(late)

        dao.getAll().test {
            val items = awaitItem()
            assertEquals(2, items.size)
            // El último insertado (mayor createdAt) debe ir primero
            assertEquals(secondId, items[0].id.toLong())
            assertEquals(firstId, items[1].id.toLong())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getById_returnsCorrectItem() = runTest {
        val rowId = dao.insert(outfit1).toInt()

        val result = dao.getById(rowId)
        assertNotNull("getById debe devolver el item", result)
        assertEquals(outfit1.shirtId, result?.shirtId)
    }

    @Test
    fun getById_nonExistent_returnsNull() = runTest {
        val result = dao.getById(9999)
        assertNull("getById con id inexistente debe devolver null", result)
    }

    @Test
    fun delete_existingItem_removesItFromDb() = runTest {
        val rowId = dao.insert(outfit1)

        var inserted: FavoriteOutfit? = null
        dao.getAll().test {
            inserted = awaitItem().first()
            cancelAndIgnoreRemainingEvents()
        }

        val deleted = dao.delete(inserted!!)
        assertEquals("delete debe devolver 1 fila eliminada", 1, deleted)

        dao.getAll().test {
            assertTrue("La lista debe estar vacía después de eliminar", awaitItem().isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteById_existingId_removesItem() = runTest {
        val rowId = dao.insert(outfit1).toInt()

        val deleted = dao.deleteById(rowId)
        assertEquals(1, deleted)

        val result = dao.getById(rowId)
        assertNull(result)
    }

    @Test
    fun deleteById_nonExistent_returns0() = runTest {
        val deleted = dao.deleteById(9999)
        assertEquals(0, deleted)
    }

    @Test
    fun delete_nonExistentItem_returns0() = runTest {
        val ghost = FavoriteOutfit(id = 9999)
        val deleted = dao.delete(ghost)
        assertEquals(0, deleted)
    }

    @Test
    fun getAll_emptyDatabase_returnsEmptyList() = runTest {
        dao.getAll().test {
            assertTrue("DB vacía debe emitir lista vacía", awaitItem().isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun insert_fieldsArePersistedCorrectly() = runTest {
        dao.insert(outfit1)

        dao.getAll().test {
            val item = awaitItem().first()
            assertEquals(1, item.shirtId)
            assertEquals(2, item.pantId)
            assertEquals(3, item.upperId)
            assertEquals(4, item.shoesId)
            assertTrue("createdAt debe ser > 0", item.createdAt > 0)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun insert_nullableFields_arePersistedAsNull() = runTest {
        dao.insert(outfit2)

        dao.getAll().test {
            val item = awaitItem().first()
            assertNull(item.pantId)
            assertNull(item.shoesId)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
