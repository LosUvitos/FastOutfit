package com.uvitos.fastoutfit.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// ClothingDao.kt — database queries
@Dao
interface ClothingDao {
    @Query("SELECT * FROM clothing_items WHERE category = :category")
    fun getByCategory(category: String): Flow<List<ClothingItem>>

    @Query("SELECT * FROM clothing_items")
    fun getAll(): Flow<List<ClothingItem>>

    @Insert
    suspend fun insert(item: ClothingItem) : Long

    @Delete
    suspend fun delete(item: ClothingItem) : Int
}