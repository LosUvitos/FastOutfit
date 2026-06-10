package com.uvitos

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.uvitos.fastoutfit.data.database.AppDatabase
import com.uvitos.fastoutfit.data.database.ClothingDao
import com.uvitos.fastoutfit.data.database.ClothingItem
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ClothingDaoTest {

    // ── Fixtures ────────────────────────────────────────────────────────────

    private lateinit var db: AppDatabase
    private lateinit var dao: ClothingDao

    private val shirtItem = ClothingItem(
        id = 0,
        name = "Camisa Blanca",
        imagePath = "/tmp/shirt1.jpg",
        category = "shirts",
    )

    private val pantItem = ClothingItem(
        id = 0,
        name = "Jeans Azul",
        imagePath = "/tmp/pant1.jpg",
        category = "pants",
    )

    private val shoeItem = ClothingItem(
        id = 0,
        name = "Tenis Negros",
        imagePath = "/tmp/shoe1.jpg",
        category = "shoes",
    )

    // ── Setup / Teardown ─────────────────────────────────────────────────────

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()   // permitido solo en tests
            .build()
        dao = db.clothingDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    // ── Tests: insert ────────────────────────────────────────────────────────

    /**
     * Insertar un item debe devolver un rowId > 0
     * y el item debe aparecer en getAll().
     */
    @Test
    fun insert_returnsPositiveRowId_andItemAppearsInGetAll() = runTest {
        val rowId = dao.insert(shirtItem)
        assertTrue("insert debe devolver rowId > 0", rowId > 0)

        dao.getAll().test {
            val items = awaitItem()
            assertEquals(1, items.size)
            assertEquals("Camisa Blanca", items.first().name)
            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * Insertar varios items y verificar que todos están presentes.
     */
    @Test
    fun insert_multipleItems_allAppearInGetAll() = runTest {
        dao.insert(shirtItem)
        dao.insert(pantItem)
        dao.insert(shoeItem)

        dao.getAll().test {
            val items = awaitItem()
            assertEquals(3, items.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // ── Tests: getByCategory ─────────────────────────────────────────────────

    /**
     * getByCategory("shirts") solo debe devolver items de esa categoría.
     */
    @Test
    fun getByCategory_returnsOnlyMatchingCategory() = runTest {
        dao.insert(shirtItem)
        dao.insert(pantItem)
        dao.insert(shoeItem)

        dao.getByCategory("shirts").test {
            val items = awaitItem()
            assertEquals(1, items.size)
            assertEquals("shirts", items.first().category)
            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * getByCategory con categoría vacía debe devolver lista vacía.
     */
    @Test
    fun getByCategory_noMatch_returnsEmptyList() = runTest {
        dao.insert(shirtItem)

        dao.getByCategory("upper").test {
            val items = awaitItem()
            assertTrue("Debe ser lista vacía", items.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * Insertar múltiples items de la misma categoría y verificar
     * que getByCategory los devuelve todos.
     */
    @Test
    fun getByCategory_multipleItemsSameCategory_returnsAll() = runTest {
        val shirt2 = shirtItem.copy(name = "Camisa Negra", imagePath = "/tmp/shirt2.jpg")
        val shirt3 = shirtItem.copy(name = "Camisa Roja",  imagePath = "/tmp/shirt3.jpg")

        dao.insert(shirtItem)
        dao.insert(shirt2)
        dao.insert(shirt3)
        dao.insert(pantItem)

        dao.getByCategory("shirts").test {
            val items = awaitItem()
            assertEquals(3, items.size)
            assertTrue(items.all { it.category == "shirts" })
            cancelAndIgnoreRemainingEvents()
        }
    }

    // ── Tests: delete ────────────────────────────────────────────────────────

    /**
     * delete debe devolver 1 (número de filas afectadas) y el item
     * no debe aparecer en getAll().
     */
    @Test
    fun delete_existingItem_removesItFromDb() = runTest {
        val rowId = dao.insert(shirtItem)

        // Recuperamos el item con su id real asignado por Room
        var insertedItem: ClothingItem? = null
        dao.getAll().test {
            insertedItem = awaitItem().first()
            cancelAndIgnoreRemainingEvents()
        }

        val deleted = dao.delete(insertedItem!!)
        assertEquals("delete debe devolver 1 fila eliminada", 1, deleted)

        dao.getAll().test {
            val items = awaitItem()
            assertTrue("La lista debe estar vacía después de eliminar", items.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * Eliminar un item no debe afectar al resto de la tabla.
     */
    @Test
    fun delete_oneItem_otherItemsRemain() = runTest {
        dao.insert(shirtItem)
        dao.insert(pantItem)

        var items: List<ClothingItem> = emptyList()
        dao.getAll().test {
            items = awaitItem()
            cancelAndIgnoreRemainingEvents()
        }

        val shirtInserted = items.first { it.category == "shirts" }
        dao.delete(shirtInserted)

        dao.getAll().test {
            val remaining = awaitItem()
            assertEquals(1, remaining.size)
            assertEquals("pants", remaining.first().category)
            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * Intentar eliminar un item cuyo id no existe en la tabla
     * debe devolver 0 filas afectadas.
     */
    @Test
    fun delete_nonExistentItem_returns0() = runTest {
        val ghostItem = shirtItem.copy(id = 9999)
        val deleted = dao.delete(ghostItem)
        assertEquals(
            "Eliminar un item que no existe debe devolver 0",
            0,
            deleted
        )
    }

    // ── Tests: getAll vacío ──────────────────────────────────────────────────

    /**
     * Sin inserciones, getAll() debe emitir una lista vacía.
     */
    @Test
    fun getAll_emptyDatabase_returnsEmptyList() = runTest {
        dao.getAll().test {
            val items = awaitItem()
            assertTrue("DB vacía debe emitir lista vacía", items.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    // ── Tests: integridad del modelo ─────────────────────────────────────────

    /**
     * Los campos del item insertado deben conservarse tal como se guardaron.
     */
    @Test
    fun insert_fieldsArePersistedCorrectly() = runTest {
        dao.insert(pantItem)

        dao.getAll().test {
            val item = awaitItem().first()
            assertEquals("Jeans Azul",   item.name)
            assertEquals("/tmp/pant1.jpg", item.imagePath)
            assertEquals("pants",          item.category)
            assertTrue("createdAt debe ser > 0", item.createdAt > 0)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
