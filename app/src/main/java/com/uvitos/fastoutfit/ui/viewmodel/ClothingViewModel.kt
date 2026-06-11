package com.uvitos.fastoutfit.ui.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uvitos.fastoutfit.data.database.ClothingItem
import com.uvitos.fastoutfit.data.database.FavoriteOutfit
import com.uvitos.fastoutfit.data.repository.ClothingRepository
import com.uvitos.fastoutfit.data.repository.FavoriteOutfitRepository
import com.uvitos.fastoutfit.navigation.Categories
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class ClothingViewModel(
    private val repository: ClothingRepository,
    private val favoriteOutfitRepository: FavoriteOutfitRepository
) : ViewModel() {

    // State the screen can observe
    private val _selectedCategory = MutableStateFlow(Categories.SHIRTS)
    val selectedCategory: StateFlow<String> = _selectedCategory

    data class Outfit(
        val shirt: ClothingItem?,
        val pant: ClothingItem?,
        val upper: ClothingItem?,
        val shoes: ClothingItem?
    )
    private val _randomOutfit = MutableStateFlow(
        Outfit(
            shirt = null,
            pant = null,
            upper = null,
            shoes = null
        )
    )

    val randomOutfit: StateFlow<Outfit> = _randomOutfit

    val favoriteOutfits: StateFlow<List<FavoriteOutfit>> =
        favoriteOutfitRepository.getAll()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun generateRandomOutfit() {

        val garments = all.value

        val shirts = garments.filter {
            it.category == Categories.SHIRTS
        }

        val pants = garments.filter {
            it.category == Categories.PANTS
        }

        val uppers = garments.filter {
            it.category == Categories.UPPER
        }

        val shoes = garments.filter {
            it.category == Categories.SHOES
        }

        _randomOutfit.value = Outfit(
            shirt = shirts.randomOrNull(),
            pant = pants.randomOrNull(),
            upper = uppers.randomOrNull(),
            shoes = shoes.randomOrNull()
        )
    }

    val shirts: StateFlow<List<ClothingItem>> =
        repository.getByCategory("shirts")
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val all: StateFlow<List<ClothingItem>> =
        repository.getAllClothes()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val visibleGarments: StateFlow<List<ClothingItem>> = combine(all, selectedCategory) { items, category -> items.filter { it.category == category }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }
    fun deleteItem(item: ClothingItem) {
        viewModelScope.launch {
            if (item.imagePath.isNotEmpty()) {
                val file = File(item.imagePath)
                if (file.exists()) {
                    file.delete()
                    Log.d("ClothingVM", "🗑 Imagen borrada: ${item.imagePath}")
                }
            }
            repository.delete(item)
        }
    }

    fun saveItem(item: ClothingItem) {
        viewModelScope.launch {
            repository.insert(item)
        }
    }

    fun saveClothingItem(
        context: Context,
        imageUri: Uri,
        name: String,
        category: String,
    ) {
        viewModelScope.launch {
            // 1. Copy image from temp URI to permanent app folder
            val permanentPath = saveImageToStorage(context, imageUri)

            // 2. Save item with path to database
            val item = ClothingItem(
                name = name,
                imagePath = permanentPath,
                category = category,
            )
            saveItem(item)
        }
    }

    fun saveCurrentOutfitAsFavorite() {
        val outfit = _randomOutfit.value
        if (outfit.shirt == null && outfit.pant == null && outfit.upper == null && outfit.shoes == null) return
        viewModelScope.launch {
            val favorite = FavoriteOutfit(
                shirtId = outfit.shirt?.id,
                pantId = outfit.pant?.id,
                upperId = outfit.upper?.id,
                shoesId = outfit.shoes?.id
            )
            favoriteOutfitRepository.insert(favorite)
        }
    }

    fun deleteFavoriteOutfit(outfit: FavoriteOutfit) {
        viewModelScope.launch {
            favoriteOutfitRepository.delete(outfit)
        }
    }

    fun resolveFavoriteOutfit(outfit: FavoriteOutfit): Outfit {
        val items = all.value
        return Outfit(
            shirt = items.find { it.id == outfit.shirtId },
            pant = items.find { it.id == outfit.pantId },
            upper = items.find { it.id == outfit.upperId },
            shoes = items.find { it.id == outfit.shoesId }
        )
    }

    // Copies the photo to a permanent location
    private fun saveImageToStorage(context: Context, uri: Uri): String {
        val fileName = "outfit_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, "Pictures/$fileName")
        file.parentFile?.mkdirs()

        context.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        return file.absolutePath
    }
    init {
        viewModelScope.launch {
            all.collect { garments ->

                if (garments.isNotEmpty()) {
                    generateRandomOutfit()
                }
            }
        }
    }
}

