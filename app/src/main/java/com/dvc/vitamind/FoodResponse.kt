package com.dvc.vitamind

data class FoodResponse(
    val foods: List<Food>
)

data class Food(
    val fdcId: Int,
    val description: String,
    val foodNutrients: List<Nutrient>
)

data class Nutrient(
    val nutrientName: String,
    val unitName: String,
    val value: Double,
)
