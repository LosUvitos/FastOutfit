package com.uvitos.fastoutfit.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clothing_items")
data class ClothingItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val imagePath: String = "",   // "/data/.../outfit_123.jpg"
    val category: String = "",    // "shirts", "pants", etc.
    val createdAt: Long = System.currentTimeMillis()
)