package com.uvitos.fastoutfit.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [ClothingItem::class, FavoriteOutfit::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun clothingDao(): ClothingDao
    abstract fun favoriteOutfitDao(): FavoriteOutfitDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2 = Migration(1, 2) { db ->
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS favorite_outfits (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    shirtId INTEGER,
                    pantId INTEGER,
                    upperId INTEGER,
                    shoesId INTEGER,
                    createdAt INTEGER NOT NULL
                )
                """.trimIndent()
            )
        }

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fastoutfit_db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
