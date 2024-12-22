package com.dvc.vitamind.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dvc.vitamind.model.FoodNutrient

@Database(entities = [FoodNutrient::class], version = 1, exportSchema = false)
abstract class FoodDatabase : RoomDatabase() {

    abstract fun foodNutrientDao(): FoodNutrientDao

    companion object {
        @Volatile
        private var INSTANCE: FoodDatabase? = null

        // Singleton pattern ile veritabanı örneğini alıyoruz
        fun getDatabase(context: Context): FoodDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FoodDatabase::class.java,
                    "food_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
