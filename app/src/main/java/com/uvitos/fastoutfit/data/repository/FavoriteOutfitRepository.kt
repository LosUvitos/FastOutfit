package com.uvitos.fastoutfit.data.repository

import com.uvitos.fastoutfit.data.database.FavoriteOutfit
import com.uvitos.fastoutfit.data.database.FavoriteOutfitDao
import kotlinx.coroutines.flow.Flow

class FavoriteOutfitRepository(private val dao: FavoriteOutfitDao) {
    fun getAll(): Flow<List<FavoriteOutfit>> = dao.getAll()
    suspend fun getById(id: Int): FavoriteOutfit? = dao.getById(id)
    suspend fun insert(item: FavoriteOutfit): Long = dao.insert(item)
    suspend fun delete(item: FavoriteOutfit): Int = dao.delete(item)
    suspend fun deleteById(id: Int): Int = dao.deleteById(id)
}
