package com.uvitos.fastoutfit

import app.cash.turbine.test
import com.uvitos.fastoutfit.data.database.ClothingItem
import com.uvitos.fastoutfit.data.database.FavoriteOutfit
import com.uvitos.fastoutfit.data.repository.ClothingRepository
import com.uvitos.fastoutfit.data.repository.FavoriteOutfitRepository
import com.uvitos.fastoutfit.ui.viewmodel.ClothingViewModel
import com.uvitos.fastoutfit.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Pruebas unitarias para los métodos de outfits favoritos
 * en [ClothingViewModel].
 *
 * Usa MockK para simular los repositorios.
 *
 * Cómo correr:
 *   ./gradlew test
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ClothingViewModelFavoriteTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: ClothingViewModel
    private val clothingRepo: ClothingRepository = mockk(relaxed = true)
    private val favoriteRepo: FavoriteOutfitRepository = mockk(relaxed = true)

    private val shirt = ClothingItem(
        id = 1, name = "Camisa", imagePath = "/tmp/shirt.jpg", category = "shirts"
    )
    private val pant = ClothingItem(
        id = 2, name = "Pantalón", imagePath = "/tmp/pant.jpg", category = "pants"
    )
    private val upper = ClothingItem(
        id = 3, name = "Chamarra", imagePath = "/tmp/upper.jpg", category = "upper"
    )
    private val shoes = ClothingItem(
        id = 4, name = "Zapatos", imagePath = "/tmp/shoes.jpg", category = "shoes"
    )

    private val allItems = MutableStateFlow(listOf(shirt, pant, upper, shoes))
    private val favFlow = MutableStateFlow<List<FavoriteOutfit>>(emptyList())

    @Before
    fun setUp() {
        coEvery { clothingRepo.getAllClothes() } returns allItems
        coEvery { favoriteRepo.getAll() } returns favFlow
        viewModel = ClothingViewModel(clothingRepo, favoriteRepo)
    }

    // ── saveCurrentOutfitAsFavorite ──────────────────────────────────────────

    @Test
    fun saveCurrentOutfitAsFavorite_withGeneratedOutfit_insertsFavorite() = runTest {
        viewModel.generateRandomOutfit()

        viewModel.saveCurrentOutfitAsFavorite()

        coVerify(exactly = 1) { favoriteRepo.insert(any()) }
    }

    @Test
    fun saveCurrentOutfitAsFavorite_persistsCorrectIds() = runTest {
        viewModel.generateRandomOutfit()

        var capturedOutfit: FavoriteOutfit? = null
        coEvery { favoriteRepo.insert(any()) } answers {
            capturedOutfit = firstArg()
            1L
        }

        viewModel.saveCurrentOutfitAsFavorite()

        assertNotNull("Debe haberse insertado un FavoriteOutfit", capturedOutfit)
        val currentOutfit = viewModel.randomOutfit.value

        assertEquals(currentOutfit.shirt?.id, capturedOutfit?.shirtId)
        assertEquals(currentOutfit.pant?.id, capturedOutfit?.pantId)
        assertEquals(currentOutfit.upper?.id, capturedOutfit?.upperId)
        assertEquals(currentOutfit.shoes?.id, capturedOutfit?.shoesId)
    }

    // ── deleteFavoriteOutfit ─────────────────────────────────────────────────

    @Test
    fun deleteFavoriteOutfit_callsRepositoryDelete() = runTest {
        val fav = FavoriteOutfit(id = 1, shirtId = 1, pantId = 2)

        viewModel.deleteFavoriteOutfit(fav)

        coVerify(exactly = 1) { favoriteRepo.delete(fav) }
    }

    // ── resolveFavoriteOutfit ────────────────────────────────────────────────

    @Test
    fun resolveFavoriteOutfit_returnsCorrectOutfit() {
        val fav = FavoriteOutfit(
            id = 1,
            shirtId = 1,
            pantId = 2,
            upperId = 3,
            shoesId = 4
        )

        val resolved = viewModel.resolveFavoriteOutfit(fav)

        assertNotNull(resolved.shirt)
        assertNotNull(resolved.pant)
        assertNotNull(resolved.upper)
        assertNotNull(resolved.shoes)
        assertEquals("Camisa", resolved.shirt?.name)
        assertEquals("Pantalón", resolved.pant?.name)
        assertEquals("Chamarra", resolved.upper?.name)
        assertEquals("Zapatos", resolved.shoes?.name)
    }

    @Test
    fun resolveFavoriteOutfit_withNullIds_returnsNullFields() {
        val fav = FavoriteOutfit(id = 1, shirtId = null, pantId = null)

        val resolved = viewModel.resolveFavoriteOutfit(fav)

        assertNull(resolved.shirt)
        assertNull(resolved.pant)
        assertNull(resolved.upper)
        assertNull(resolved.shoes)
    }

    @Test
    fun resolveFavoriteOutfit_withDeletedItems_returnsNullForMissing() {
        val fav = FavoriteOutfit(
            id = 1,
            shirtId = 999,
            pantId = 2
        )

        val resolved = viewModel.resolveFavoriteOutfit(fav)

        assertNull("Item eliminado debe ser null", resolved.shirt)
        assertNotNull(resolved.pant)
    }

    // ── favoriteOutfits flow ─────────────────────────────────────────────────

    @Test
    fun favoriteOutfits_emitsFromRepository() = runTest {
        val fav = FavoriteOutfit(id = 1, shirtId = 1)
        favFlow.value = listOf(fav)

        viewModel.favoriteOutfits.test {
            val items = awaitItem()
            assertEquals(1, items.size)
            assertEquals(fav.id, items.first().id)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
