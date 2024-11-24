import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dvc.vitamind.Food

class FoodSearchViewModel : ViewModel() {
    private val _selectedFood = MutableLiveData<Food?>()
    val selectedFood: LiveData<Food?> get() = _selectedFood

    fun selectFood(food: Food) {
        _selectedFood.value = food
    }
}
