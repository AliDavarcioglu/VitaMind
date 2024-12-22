package com.dvc.vitamind.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.dvc.vitamind.model.FoodNutrient
import com.dvc.vitamind.roomdb.FoodNutrientDao
import com.dvc.vitamind.roomdb.FoodDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FoodNutrientViewModel(application: Application) : AndroidViewModel(application) {

    private val foodNutrientDao: FoodNutrientDao

    // LiveData olarak tüm besin öğelerini sağlıyoruz
    val allNutrients: LiveData<List<FoodNutrient>>

    init {
        // Room veritabanı örneğini al
        val foodDatabase = FoodDatabase.getDatabase(application)
        foodNutrientDao = foodDatabase.foodNutrientDao()

        // Veritabanından besin öğelerini almak için getAllNutrients fonksiyonunu çağırıyoruz
        allNutrients = liveData(Dispatchers.IO) {
            emit(foodNutrientDao.getAllNutrients())
        }
    }

    // Yeni bir besin öğesi eklemek için bir fonksiyon
    fun insertNutrient(nutrient: FoodNutrient) {
        viewModelScope.launch(Dispatchers.IO) {
            foodNutrientDao.insertNutrient(nutrient)
        }
    }
}
