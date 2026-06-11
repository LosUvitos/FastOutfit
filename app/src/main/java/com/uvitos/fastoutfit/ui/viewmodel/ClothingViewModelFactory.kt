package com.uvitos.fastoutfit.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uvitos.fastoutfit.data.database.AppDatabase
import com.uvitos.fastoutfit.data.repository.ClothingRepository
import com.uvitos.fastoutfit.data.repository.FavoriteOutfitRepository

class ClothingViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = AppDatabase.getInstance(context)
        val clothingRepository = ClothingRepository(db.clothingDao())
        val favoriteOutfitRepository = FavoriteOutfitRepository(db.favoriteOutfitDao())
        @Suppress("UNCHECKED_CAST")
        return ClothingViewModel(clothingRepository, favoriteOutfitRepository) as T
    }
}