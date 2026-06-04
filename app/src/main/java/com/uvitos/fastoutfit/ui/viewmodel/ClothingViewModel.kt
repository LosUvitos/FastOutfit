package com.uvitos.fastoutfit.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uvitos.fastoutfit.data.database.ClothingItem
import com.uvitos.fastoutfit.data.repository.ClothingRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class ClothingViewModel(private val repository: ClothingRepository) : ViewModel() {

    // State the screen can observe
    val shirts: StateFlow<List<ClothingItem>> =
        repository.getByCategory("shirts")
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

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

