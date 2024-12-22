package com.dvc.vitamind.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dvc.vitamind.Nutrient

@Entity(tableName = "food_nutrients")
data class FoodNutrient(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nutrientName: String,
    val value:Double,
    val unit: String?,
    val gram: Double // Add the gram field to store the user's input
)
