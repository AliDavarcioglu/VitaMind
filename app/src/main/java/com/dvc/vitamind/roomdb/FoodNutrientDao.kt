package com.dvc.vitamind.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dvc.vitamind.model.FoodNutrient

@Dao
interface FoodNutrientDao {
    @Insert
    suspend fun insertNutrient(nutrient: FoodNutrient)

    @Query("SELECT * FROM food_nutrients")
    suspend fun getAllNutrients(): List<FoodNutrient>
}
