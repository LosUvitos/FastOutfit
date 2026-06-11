package com.uvitos.fastoutfit.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteOutfitDao {
    @Query("SELECT * FROM favorite_outfits ORDER BY createdAt DESC")
    fun getAll(): Flow<List<FavoriteOutfit>>

    @Query("SELECT * FROM favorite_outfits WHERE id = :id")
    suspend fun getById(id: Int): FavoriteOutfit?

    @Insert
    suspend fun insert(item: FavoriteOutfit): Long

    @Delete
    suspend fun delete(item: FavoriteOutfit): Int

    @Query("DELETE FROM favorite_outfits WHERE id = :id")
    suspend fun deleteById(id: Int): Int
}
