package com.uvitos.fastoutfit

import app.cash.turbine.test
import com.uvitos.fastoutfit.data.database.ClothingDao
import com.uvitos.fastoutfit.data.database.ClothingItem
import com.uvitos.fastoutfit.data.repository.ClothingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Pruebas unitarias para [ClothingRepository].
 *
 * Usa un [FakeClothingDao] en lugar de la base de datos real,
 * por lo que estas pruebas corren en la JVM sin necesidad de dispositivo.
 *
 * Cómo correr:
 *   ./gradlew test
 */
class ClothingRepositoryTest {

    // ── Fake DAO ─────────────────────────────────────────────────────────────

    /**
     * Implementación en memoria del DAO para pruebas unitarias.
     * Simula el comportamiento de Room sin base de datos real.
     */
    private class FakeClothingDao : ClothingDao {

        /** Estado reactivo que imita un Flow de Room */
        private val _items = MutableStateFlow<List<ClothingItem>>(emptyList())
        private var nextId = 1

        override fun getAll(): Flow<List<ClothingItem>> = _items

        override fun getByCategory(category: String): Flow<List<ClothingItem>> =
            _items.map { list -> list.filter { it.category == category } }

        override suspend fun insert(item: ClothingItem): Long {
            val id = nextId++
            val toInsert = item.copy(id = id)
            _items.value = _items.value + toInsert
            return id.toLong()
        }

        override suspend fun delete(item: ClothingItem): Int {
            val before = _items.value.size
            _items.value = _items.value.filterNot { it.id == item.id }
            val after = _items.value.size
            return before - after          // 1 si se eliminó, 0 si no existía
        }
    }

    // ── Fixtures ─────────────────────────────────────────────────────────────

    private lateinit var fakeDao: FakeClothingDao
    private lateinit var repository: ClothingRepository

    private val shirt = ClothingItem(
        id        = 0,
        name      = "Camisa Azul",
        imagePath = "/tmp/shirt.jpg",
        category  = "shirts",
    )

    private val pant = ClothingItem(
        id        = 0,
        name      = "Pantalón Gris",
        imagePath = "/tmp/pant.jpg",
        category  = "pants",
    )

    @Before
    fun setUp() {
        fakeDao    = FakeClothingDao()
        repository = ClothingRepository(fakeDao)
    }

    // ── insert ───────────────────────────────────────────────────────────────

    @Test
    fun insert_delegatesToDao_andIsVisibleInGetAll() = runTest {
        repository.insert(shirt)

        repository.getAllClothes().test {
            val items = awaitItem()
            assertEquals(1, items.size)
            assertEquals("Camisa Azul", items.first().name)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun insert_multipleItems_allVisible() = runTest {
        repository.insert(shirt)
        repository.insert(pant)

        repository.getAllClothes().test {
            val items = awaitItem()
            assertEquals(2, items.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // ── getAllClothes ─────────────────────────────────────────────────────────

    @Test
    fun getAllClothes_emptyRepo_returnsEmptyList() = runTest {
        repository.getAllClothes().test {
            assertTrue(awaitItem().isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    // ── getByCategory ─────────────────────────────────────────────────────────

    @Test
    fun getByCategory_filtersCorrectly() = runTest {
        repository.insert(shirt)
        repository.insert(pant)

        repository.getByCategory("shirts").test {
            val items = awaitItem()
            assertEquals(1, items.size)
            assertEquals("shirts", items.first().category)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getByCategory_noMatch_returnsEmpty() = runTest {
        repository.insert(shirt)

        repository.getByCategory("shoes").test {
            assertTrue(awaitItem().isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getByCategory_reactsToNewInsertions() = runTest {
        repository.getByCategory("shirts").test {
            // Estado inicial: vacío
            assertTrue("Debe empezar vacío", awaitItem().isEmpty())

            // Insertamos y el Flow debe emitir de nuevo
            repository.insert(shirt)
            val updated = awaitItem()
            assertEquals(1, updated.size)

            cancelAndIgnoreRemainingEvents()
        }
    }

    // ── delete ────────────────────────────────────────────────────────────────

    @Test
    fun delete_removesItemFromRepo() = runTest {
        repository.insert(shirt)
        repository.insert(pant)

        // Obtenemos el item con su id asignado
        var insertedShirt: ClothingItem? = null
        repository.getAllClothes().test {
            insertedShirt = awaitItem().first { it.category == "shirts" }
            cancelAndIgnoreRemainingEvents()
        }

        repository.delete(insertedShirt!!)

        repository.getAllClothes().test {
            val items = awaitItem()
            assertEquals(1, items.size)
            assertEquals("pants", items.first().category)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun delete_nonExistentItem_doesNotAffectOthers() = runTest {
        repository.insert(shirt)

        val ghost = shirt.copy(id = 9999)
        repository.delete(ghost)

        repository.getAllClothes().test {
            assertEquals(1, awaitItem().size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // ── Reactividad ───────────────────────────────────────────────────────────

    /**
     * getAllClothes es un Flow: debe emitir un nuevo valor cada vez que
     * se inserta o elimina un item.
     */
    @Test
    fun getAllClothes_emitsNewValueOnEachChange() = runTest {
        repository.getAllClothes().test {
            // Emisión 1: vacío
            assertTrue(awaitItem().isEmpty())

            // Emisión 2: después de insertar
            repository.insert(shirt)
            assertEquals(1, awaitItem().size)

            // Emisión 3: después de insertar otro
            repository.insert(pant)
            assertEquals(2, awaitItem().size)

            // Emisión 4: después de eliminar
            val toDelete = repository.getAllClothes().let { /* evitamos anidamiento de collect */ shirt.copy(id = 1) }
            repository.delete(toDelete)
            assertEquals(1, awaitItem().size)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
