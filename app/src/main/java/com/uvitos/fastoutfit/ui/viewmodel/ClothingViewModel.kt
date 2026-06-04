package com.uvitos.fastoutfit.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uvitos.fastoutfit.data.database.ClothingItem
import com.uvitos.fastoutfit.data.repository.ClothingRepository
import com.uvitos.fastoutfit.navigation.Categories
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class ClothingViewModel(private val repository: ClothingRepository) : ViewModel() {

    // State the screen can observe
    private val _selectedCategory = MutableStateFlow(Categories.SHIRTS)
    val selectedCategory: StateFlow<String> = _selectedCategory

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
}

