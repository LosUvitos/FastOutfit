package com.uvitos.fastoutfit.data.repository

import com.uvitos.fastoutfit.data.database.ClothingDao
import com.uvitos.fastoutfit.data.database.ClothingItem

class ClothingRepository(private val dao: ClothingDao) {
    fun getByCategory(category: String) = dao.getByCategory(category)
    suspend fun insert(item: ClothingItem) = dao.insert(item)
    suspend fun delete(item: ClothingItem) = dao.delete(item)
}