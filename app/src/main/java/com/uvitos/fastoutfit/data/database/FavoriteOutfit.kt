package com.uvitos.fastoutfit.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_outfits")
data class FavoriteOutfit(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val shirtId: Int? = null,
    val pantId: Int? = null,
    val upperId: Int? = null,
    val shoesId: Int? = null,
    val createdAt: Long = System.currentTimeMillis()
)
