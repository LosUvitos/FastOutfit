package com.uvitos.fastoutfit.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uvitos.fastoutfit.data.database.AppDatabase
import com.uvitos.fastoutfit.data.repository.ClothingRepository

class ClothingViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = AppDatabase.getInstance(context)
        val repository = ClothingRepository(db.clothingDao())
        @Suppress("UNCHECKED_CAST")
        return ClothingViewModel(repository) as T
    }
}