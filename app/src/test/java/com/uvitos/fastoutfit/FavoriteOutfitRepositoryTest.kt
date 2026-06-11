package com.uvitos.fastoutfit

import app.cash.turbine.test
import com.uvitos.fastoutfit.data.database.FavoriteOutfit
import com.uvitos.fastoutfit.data.database.FavoriteOutfitDao
import com.uvitos.fastoutfit.data.repository.FavoriteOutfitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Pruebas unitarias para [FavoriteOutfitRepository].
 *
 * Usa un [FakeFavoriteOutfitDao] en lugar de la base de datos real,
 * por lo que estas pruebas corren en la JVM sin necesidad de dispositivo.
 *
 * Cómo correr:
 *   ./gradlew test
 */
class FavoriteOutfitRepositoryTest {

    // ── Fake DAO ─────────────────────────────────────────────────────────────

    private class FakeFavoriteOutfitDao : FavoriteOutfitDao {

        private val _items = MutableStateFlow<List<FavoriteOutfit>>(emptyList())
        private var nextId = 1

        override fun getAll(): Flow<List<FavoriteOutfit>> = _items

        override suspend fun getById(id: Int): FavoriteOutfit? =
            _items.value.find { it.id == id }

        override suspend fun insert(item: FavoriteOutfit): Long {
            val id = nextId++
            val toInsert = item.copy(id = id)
            _items.value = _items.value + toInsert
            return id.toLong()
        }

        override suspend fun delete(item: FavoriteOutfit): Int {
            val before = _items.value.size
            _items.value = _items.value.filterNot { it.id == item.id }
            val after = _items.value.size
            return before - after
        }

        override suspend fun deleteById(id: Int): Int {
            val before = _items.value.size
            _items.value = _items.value.filterNot { it.id == id }
            val after = _items.value.size
            return before - after
        }
    }

    // ── Fixtures ─────────────────────────────────────────────────────────────

    private lateinit var fakeDao: FakeFavoriteOutfitDao
    private lateinit var repository: FavoriteOutfitRepository

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
    fun setUp() {
        fakeDao = FakeFavoriteOutfitDao()
        repository = FavoriteOutfitRepository(fakeDao)
    }

    // ── insert ───────────────────────────────────────────────────────────────

    @Test
    fun insert_delegatesToDao_andIsVisibleInGetAll() = runTest {
        repository.insert(outfit1)

        repository.getAll().test {
            val items = awaitItem()
            assertEquals(1, items.size)
            assertEquals(1, items.first().shirtId)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun insert_multipleItems_allVisible() = runTest {
        repository.insert(outfit1)
        repository.insert(outfit2)

        repository.getAll().test {
            val items = awaitItem()
            assertEquals(2, items.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // ── getAll ───────────────────────────────────────────────────────────────

    @Test
    fun getAll_emptyRepo_returnsEmptyList() = runTest {
        repository.getAll().test {
            assertTrue(awaitItem().isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    // ── getById ──────────────────────────────────────────────────────────────

    @Test
    fun getById_returnsInsertedItem() = runTest {
        val id = repository.insert(outfit1).toInt()

        val result = repository.getById(id)
        assertNotNull(result)
        assertEquals(outfit1.shirtId, result?.shirtId)
    }

    @Test
    fun getById_nonExistent_returnsNull() = runTest {
        val result = repository.getById(9999)
        assertNull(result)
    }

    // ── delete ────────────────────────────────────────────────────────────────

    @Test
    fun delete_removesItemFromRepo() = runTest {
        repository.insert(outfit1)
        repository.insert(outfit2)

        var inserted: FavoriteOutfit? = null
        repository.getAll().test {
            inserted = awaitItem().first()
            cancelAndIgnoreRemainingEvents()
        }

        repository.delete(inserted!!)

        repository.getAll().test {
            assertEquals(1, awaitItem().size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun delete_nonExistentItem_doesNotAffectOthers() = runTest {
        repository.insert(outfit1)

        val ghost = outfit1.copy(id = 9999)
        repository.delete(ghost)

        repository.getAll().test {
            assertEquals(1, awaitItem().size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // ── deleteById ───────────────────────────────────────────────────────────

    @Test
    fun deleteById_removesCorrectItem() = runTest {
        val id = repository.insert(outfit1).toInt()
        repository.insert(outfit2)

        repository.deleteById(id)

        repository.getAll().test {
            val items = awaitItem()
            assertEquals(1, items.size)
            assertNotEquals(id, items.first().id)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteById_nonExistent_returns0() = runTest {
        val deleted = repository.deleteById(9999)
        assertEquals(0, deleted)
    }

    // ── Reactividad ───────────────────────────────────────────────────────────

    @Test
    fun getAll_emitsNewValueOnEachChange() = runTest {
        repository.getAll().test {
            assertTrue(awaitItem().isEmpty())

            repository.insert(outfit1)
            assertEquals(1, awaitItem().size)

            repository.insert(outfit2)
            assertEquals(2, awaitItem().size)

            val toDelete = outfit1.copy(id = 1)
            repository.delete(toDelete)
            assertEquals(1, awaitItem().size)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
